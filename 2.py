new commit
# 03_gorusme_dizisi_olusturma.py
# Bu script, asistan ile kullanıcı arasında yeni bir konuşma başlatır.
# Her 'thread', bağımsız bir konuşma geçmişini temsil eder.

from P1_kurulum_ve_istemci import client

# Yeni bir görüşme dizisi (thread) oluştur
thread = client.beta.threads.create()

# Oluşturulan thread'in kimliğini (ID) yazdır.
# Bu ID, bu spesifik konuşmaya mesaj eklemek için gereklidir.
print(f"Yeni görüşme dizisi (thread) başarıyla oluşturuldu. ID: {thread.id}")

# Bu 'thread.id'yi kullanıcı oturumlarıyla ilişkilendirerek
# birden fazla kullanıcı için ayrı konuşmalar yürütebilirsiniz.

# 04_mesaj_ekleme_ve_calistirma.py
# Bu script, kullanıcının mesajını belirli bir 'thread'e ekler
# ve asistandan bu mesaja yanıt vermesini ister.

from P1_kurulum_ve_istemci import client

# Önceki adımlarda oluşturulan ID'leri buraya ekleyin
ASSISTANT_ID = "asistan_id_buraya_gelecek"  # 02_asistan_olusturma.py çıktısı
THREAD_ID = "thread_id_buraya_gelecek"    # 03_gorusme_dizisi_olusturma.py çıktısı

# Kullanıcının mesajını 'thread'e ekle
client.beta.threads.messages.create(
    thread_id=THREAD_ID,
    role="user",
    content="Türkiye'deki yapay zeka pazarının 2024 yılındaki durumu hakkında "
            "kısa bir özet sunar mısın?"
)

# Asistanı bu 'thread' üzerinde çalıştırarak bir yanıt oluşturmasını sağla
run = client.beta.threads.runs.create(
  thread_id=THREAD_ID,
  assistant_id=ASSISTANT_ID
)


print(f"Asistan çalıştırıldı. Run ID: {run.id}")
