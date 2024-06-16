import cv2
import numpy as np
import tensorflow as tf
import os

# Muat model dari file .h5
model_path = 'optimized_model.h5'
model = tf.keras.models.load_model(model_path)

# Path to the dataset (untuk mendapatkan nama kelas)
Dataset = r"C:\Users\ASUS\Downloads\dataset\SIBI"
classes = sorted(os.listdir(Dataset))  # Memastikan urutan sesuai dengan training

# Parameter gambar
img_height, img_width = 128, 128

# Fungsi untuk memproses gambar
def preprocess_image(image):
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    image = cv2.resize(image, (img_height, img_width))
    image = np.expand_dims(image, axis=0)
    image = image / 255.0
    return image

# Menggunakan kamera untuk menangkap gambar dan memprediksi
cap = cv2.VideoCapture(0)

if not cap.isOpened():
    print("Error: Could not open camera.")
else:
    print("Tekan 'q' untuk keluar")
    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # Pratinjau kamera
        cv2.imshow('Camera', frame)

        # Prakondisi dan prediksi
        processed_image = preprocess_image(frame)
        predictions = model.predict(processed_image)
        predicted_class = np.argmax(predictions[0])
        predicted_label = classes[predicted_class]
        confidence = predictions[0][predicted_class]

        # Tampilkan hasil prediksi dan persentase pada frame kamera
        cv2.putText(frame, f'Prediksi: {predicted_label} ({confidence * 100:.2f}%)', (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2, cv2.LINE_AA)
        cv2.imshow('Camera', frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

cap.release()
cv2.destroyAllWindows()
