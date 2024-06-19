const express = require('express');
const router = express.Router();
const admin = require('firebase-admin');

const db = admin.firestore();

// Get Yes or No Quiz Questions
router.get('/questions', async (req, res) => {
    try {
        const quizCollection = db.collection('yesOrNoQuizzes');
        const quizSnapshot = await quizCollection.get();
        const quizList = quizSnapshot.docs.map(doc => ({
            id: doc.id,
            ...doc.data()
        }));

        // Randomly select 10 questions
        const selectedQuestions = [];
        const totalQuestions = quizList.length;
        const questionIndices = new Set();
        while (questionIndices.size < 10) {
            const randomIndex = Math.floor(Math.random() * totalQuestions);
            if (!questionIndices.has(randomIndex)) {
                questionIndices.add(randomIndex);
                selectedQuestions.push(quizList[randomIndex]);
            }
        }

        res.status(200).json(selectedQuestions);
    } catch (error) {
        res.status(500).json({ error: 'Error getting quiz questions' });
    }
});

// Submit Yes or No Quiz Answers
router.post('/submit', async (req, res) => {
    try {
        const answers = req.body.answers; // Array of answers from the client
        const quizCollection = db.collection('yesOrNoQuizzes');
        const quizSnapshot = await quizCollection.get();
        const quizList = quizSnapshot.docs.map(doc => ({
            id: doc.id,
            ...doc.data()
        }));

        let score = 0;
        for (const answer of answers) {
            const question = quizList.find(q => q.id === answer.id);
            if (question && question.answer === answer.answer) {
                score++;
            }
        }

        res.status(200).json({ score: score, total: answers.length });
    } catch (error) {
        res.status(500).json({ error: 'Error submitting quiz answers' });
    }
});

module.exports = router;
