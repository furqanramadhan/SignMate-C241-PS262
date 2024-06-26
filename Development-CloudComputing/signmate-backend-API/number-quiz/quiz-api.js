const express = require('express');
const router = express.Router();
const admin = require('firebase-admin');

const db = admin.firestore();

// Get Number Quiz Questions
router.get('/questions', async (req, res) => {
    try {
        const quizCollection = db.collection('quizzes');
        const quizSnapshot = await quizCollection.get();
        let quizList = quizSnapshot.docs.map(doc => {
            let data = doc.data();
            data.id = doc.id; // Menambahkan ID dokumen ke dalam data
            return data;
        });
        quizList = shuffle(quizList); // Mengacak pertanyaan
        res.status(200).json(quizList);
    } catch (error) {
        res.status(500).json({ error: 'Error getting quiz questions' });
    }
});

// Shuffle function
function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

// Submit Number Quiz Answers
router.post('/submit', async (req, res) => {
    try {
        const answers = req.body.answers; // Array jawaban dari klien dengan ID pertanyaan
        let score = 0;

        for (const answer of answers) {
            const questionDoc = await db.collection('quizzes').doc(answer.id).get();
            if (questionDoc.exists && questionDoc.data().answer === answer.answer) {
                score++;
            }
        }

        res.status(200).json({ score: score, total: answers.length });
    } catch (error) {
        res.status(500).json({ error: 'Error submitting quiz answers' });
    }
});

module.exports = router;
