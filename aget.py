import os
import zipfile
import requests
from flask import Flask, request, send_file, jsonify
import socket

app = Flask(__name__)

# Конфигурация агента
AGENT_ID = "unique-client-id"
PROXY_SERVER_URL = "http://localhost:8080/api/proxy/register-agent"
AGENT_PORT = 5001
ARCHIVE_NAME = "collected_code.zip"  # Название архива

# Функция для получения пути к папке `src`, которая находится рядом с исполняемым файлом агента
def get_src_directory():
    current_dir = os.path.dirname(os.path.abspath(__file__))
    src_dir = os.path.join(current_dir, 'src')
    return src_dir

# Функция для получения пути к архиву
def get_archive_path():
    current_dir = os.path.dirname(os.path.abspath(__file__))
    return os.path.join(current_dir, ARCHIVE_NAME)

# Функция для получения локального IP-адреса
def get_local_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.settimeout(1)
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
    except Exception:
        ip = "127.0.0.1"  # В случае ошибки вернем localhost
    finally:
        s.close()
    return ip

# Регистрация агента на сервере-прокси
def register_agent():
    ip = get_local_ip()  # Используем локальный IP-адрес агента
    payload = {
        "clientId": AGENT_ID,
        "ip": ip
    }
    response = requests.post(PROXY_SERVER_URL, json=payload)
    if response.status_code == 200:
        print(f"Agent registered successfully with IP: {ip}")
    else:
        print(f"Failed to register agent: {response.status_code}")

# Функция для рекурсивного обхода директории и сбора текстовых файлов
def collect_files(directory):
    collected_files = []
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(('.txt', '.java', '.py')):  # Добавьте нужные расширения
                full_path = os.path.join(root, file)
                collected_files.append(full_path)
    return collected_files

# Создание ZIP-архива с текстовыми файлами
def create_zip_archive(files, archive_path):
    with zipfile.ZipFile(archive_path, 'w') as zipf:
        for file in files:
            zipf.write(file, os.path.relpath(file, os.path.commonpath(files)))

# Обработчик запроса на сбор кода
@app.route('/collect_code', methods=['POST'])
def collect_code():
    src_directory = get_src_directory()
    archive_path = get_archive_path()

    if not os.path.exists(src_directory):
        return jsonify({"error": "Directory 'src' not found"}), 400

    # Удаляем существующий архив, если он есть
    if os.path.exists(archive_path):
        os.remove(archive_path)

    try:
        # Сбор файлов и создание ZIP-архива
        files = collect_files(src_directory)
        create_zip_archive(files, archive_path)

        # Отправляем ZIP-архив
        return send_file(archive_path, as_attachment=True, download_name='collected_code.zip')
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    register_agent()
    app.run(host='0.0.0.0', port=AGENT_PORT)
