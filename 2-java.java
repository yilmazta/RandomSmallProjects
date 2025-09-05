import com.openai.client.OpenAiClient;
import com.openai.client.okhttp.OpenAiOkHttpClient;
import com.openai.models.BetaThread;
import com.openai.models.BetaThreadCreateParams;
import com.openai.models.BetaThreadMessageCreateParams;
import com.openai.models.BetaThreadRun;
import com.openai.models.BetaThreadRunCreateParams;

/**
 * OpenAI Asistan API'sini yönetmek için tüm işlevleri içeren ana sınıf.
 * Bu sınıf, istemci oluşturma, thread yönetimi ve asistan çalıştırma işlemlerini birleştirir.
 */
public class OpenAiAssistantManager {

    // --- BÖLÜM 1: İSTEMCİ (CLIENT) YÖNETİMİ ---

    /**
     * OpenAI API istemcisini yönetmek için bir Singleton (tek nesne) sağlayıcı.
     * Bu, uygulama boyunca aynı istemci nesnesinin kullanılmasını sağlar.
     */
    private static class OpenAiClientProvider {
        private static final String API_KEY = System.getenv("OPENAI_API_KEY");
        private static volatile OpenAiClient instance;

        // Dışarıdan nesne oluşturulmasını engelle
        private OpenAiClientProvider() {}

        /**
         * API istemci nesnesini döndürür. Eğer nesne henüz oluşturulmamışsa, oluşturur.
         * @return OpenAiClient nesnesi
         */
        public static OpenAiClient getInstance() {
            if (instance == null) {
                synchronized (OpenAiClientProvider.class) {
                    if (instance == null) {
                        if (API_KEY == null || API_KEY.trim().isEmpty()) {
                            throw new IllegalStateException("OPENAI_API_KEY ortam değişkeni ayarlanmamış veya boş.");
                        }
                        instance = OpenAiOkHttpClient.builder()
                                .apiKey(API_KEY)
                                .build();
                    }
                }
            }
            return instance;
        }
    }


    // --- BÖLÜM 2: GÖRÜŞME DİZİSİ (THREAD) OLUŞTURMA ---

    /**
     * Yeni ve boş bir görüşme dizisi (thread) oluşturur ve ID'sini yazdırır.
     * @return Oluşturulan yeni thread'in ID'si.
     */
    public static String createNewThread() {
        System.out.println("Yeni bir görüşme dizisi (thread) oluşturuluyor...");
        try {
            OpenAiClient client = OpenAiClientProvider.getInstance();
            BetaThreadCreateParams params = BetaThreadCreateParams.builder().build();
            BetaThread thread = client.beta().threads().create(params).join();
            String threadId = thread.id();
            System.out.println("Görüşme dizisi başarıyla oluşturuldu. ID: " + threadId);
            return threadId;
        } catch (Exception e) {
            System.err.println("Görüşme dizisi oluşturulurken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    // --- BÖLÜM 3: MESAJ EKLEME VE ASİSTANI ÇALIŞTIRMA ---

    /**
     * Belirtilen bir görüşme dizisine kullanıcı mesajı ekler ve asistanı çalıştırır.
     * @param threadId Mesajın ekleneceği thread'in ID'si.
     * @param assistantId Çalıştırılacak asistanın ID'si.
     */
    public static void addMessageAndRunAssistant(String threadId, String assistantId) {
        System.out.printf("\n'%s' ID'li görüşmeye mesaj ekleniyor ve asistan çalıştırılıyor...\n", threadId);
        
        if (assistantId == null || assistantId.trim().isEmpty() || assistantId.equals("asistan_id_buraya_gelecek")) {
             System.err.println("Geçerli bir Asistan ID'si girmelisiniz.");
             return;
        }

        try {
            OpenAiClient client = OpenAiClientProvider.getInstance();

            // 1. Kullanıcının mesajını 'thread'e ekle
            BetaThreadMessageCreateParams messageParams = BetaThreadMessageCreateParams.builder()
                    .threadId(threadId)
                    .role(BetaThreadMessageCreateParams.Role.USER)
                    .content("Türkiye'deki yapay zeka pazarının 2024 yılındaki durumu hakkında kısa bir özet sunar mısın?")
                    .build();
            
            client.beta().threads().messages().create(messageParams).join();
            System.out.println("Mesaj başarıyla eklendi.");

            // 2. Asistanı bu 'thread' üzerinde çalıştırarak bir yanıt oluşturmasını sağla
            BetaThreadRunCreateParams runParams = BetaThreadRunCreateParams.builder()
                    .threadId(threadId)
                    .assistantId(assistantId)
                    .build();
            
            BetaThreadRun run = client.beta().threads().runs().create(runParams).join();
            System.out.println("Asistan çalıştırıldı. Run ID: " + run.id());
            System.out.println("Not: Yanıtı almak için bu Run ID'si ile durumu periyodik olarak kontrol etmelisiniz.");

        } catch (Exception e) {
            System.err.println("Mesaj eklenirken veya asistan çalıştırılırken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // --- ANA ÇALIŞTIRMA METODU ---

    /**
     * Programın ana giriş noktası.
     * @param args Komut satırı argümanları (kullanılmıyor).
     */
    public static void main(String[] args) {
        // LÜTFEN BU DEĞİŞKENİ KENDİ ASİSTAN ID'NİZ İLE GÜNCELLEYİN!
        final String ASSISTANT_ID = "asistan_id_buraya_gelecek"; 
        
        // Adım 1: Yeni bir konuşma başlatmak için bir thread oluştur.
        // Bu metot, bir sonraki adımda kullanılacak olan Thread ID'yi döndürür.
        String newThreadId = createNewThread();
        
        // Adım 2: Oluşturulan bu thread'e bir mesaj ekle ve asistanı çalıştır.
        // Eğer bir thread başarıyla oluşturulduysa devam et.
        if (newThreadId != null) {
            // Yukarıda oluşturulan 'newThreadId'yi ve kendi asistan ID'nizi kullanarak
            // asistanı çalıştırın.
            addMessageAndRunAssistant(newThreadId, ASSISTANT_ID);
        }
    }
}
