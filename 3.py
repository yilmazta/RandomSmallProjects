# 05_yaniti_alma_ve_gosterme.py
# Bu script, asistanın çalışmasının tamamlanmasını bekler,
# ardından son yanıtı alır ve ekrana yazdırır.

import time
from P1_kurulum_ve_istemci import client

# Önceki adımlarda kullanılan ID'leri buraya ekleyin
THREAD_ID = "thread_id_buraya_gelecek"  # 04_mesaj_ekleme_ve_calistirma.py'deki ID
RUN_ID = "run_id_buraya_gelecek"  # 04_mesaj_ekleme_ve_calistirma.py çıktısı

# Yanıt hazır olana kadar bekle
while True:
    run_status = client.beta.threads.runs.retrieve(thread_id=THREAD_ID, run_id=RUN_ID)
    if run_status.status == "completed":
        break
    print("Asistan düşünüyor...")
    time.sleep(1)  # API'ye sürekli yüklenmemek için kısa bir bekleme

# 'Thread'deki mesajları listele
messages = client.beta.threads.messages.list(thread_id=THREAD_ID)

# Asistanın en son yanıtını bul ve yazdır
assistant_response = messages.data[0].content[0].text.value
print("\nAsistanın Yanıtı:")
print(assistant_response)
