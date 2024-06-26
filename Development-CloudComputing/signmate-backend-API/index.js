const express = require('express');
const bodyParser = require('body-parser');
const admin = require('firebase-admin');
const app = express();
const port = 3000;

// Initialize Firebase Admin
const serviceAccount = require('./serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://signmate-425105.firebaseio.com' // Ganti dengan URL database Anda
});

// Middleware untuk mengurai body dari permintaan
app.use(bodyParser.json());

// Routes untuk Alphabet Quiz
const alphabetQuizRoutes = require('./alphabet-quiz/quiz-api');
app.use('/alphabet-quiz', alphabetQuizRoutes);

// Routes untuk Yes or No Quiz
const yesOrNoQuizRoutes = require('./yes-or-no-quiz/quiz-api');
app.use('/yes-or-no-quiz', yesOrNoQuizRoutes);

// Routes untuk Number Quiz
const numberQuizRoutes = require('./number-quiz/quiz-api');
app.use('/number-quiz', numberQuizRoutes);

// Routes untuk Edit Profile
const editProfileRoutes = require('./edit-profile/edit-api');
app.use('/user', editProfileRoutes);

// Root Route
app.get('/', (req, res) => {
  res.send('Welcome to SignMate Backend API');
});

// Jalankan server pada port yang ditentukan
app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
