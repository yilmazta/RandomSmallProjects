#Github-n8n commit
# 01_kurulum_ve_istemci.py
# Bu script, OpenAI API ile bağlantı kurmak için istemciyi başlatır.
# API anahtarınızı ortam değişkenlerinden güvenli bir şekilde yükler.

import openai
import os

# OpenAI istemcisini başlat
# Kütüphane, 'OPENAI_API_KEY' adlı ortam değişkenini otomatik olarak arar.
client = openai.OpenAI()

print("OpenAI istemcisi başarıyla başlatıldı.")
# Bu 'client' nesnesi, projenin diğer tüm bölümlerinde API ile
# iletişim kurmak için kullanılacaktır.

# 02_asistan_olusturma.py
# Bu script, projemiz için özel bir AI asistanı oluşturur.
# Asistanın görevini, talimatlarını ve kullanacağı modeli burada tanımlarız.

from P1_kurulum_ve_istemci import client

# Asistanı oluştur
assistant = client.beta.assistants.create(
    name="Veri Analizi Uzmanı",
    instructions="Sen, verilen verileri analiz eden ve özetleyen bir uzmansın. "
                 "Sana sunulan metinlerdeki ana temaları ve önemli noktaları bul.",
    model="gpt-4-turbo", # Veya başka bir uygun model
)

# Oluşturulan asistanın kimliğini (ID) yazdır. Bu ID'yi daha sonra kullanacağız.
print(f"Asistan başarıyla oluşturuldu. ID: {assistant.id}")

# Bu ID'yi bir dosyaya veya veritabanına kaydederek her seferinde

# yeni bir asistan oluşturmaktan kaçınabilirsiniz.
