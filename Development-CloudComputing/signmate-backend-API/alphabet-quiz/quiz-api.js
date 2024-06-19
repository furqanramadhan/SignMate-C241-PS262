const express = require('express');
const router = express.Router();
const admin = require('firebase-admin');

const db = admin.firestore();

function shuffle(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

// Get Alphabet Quiz Questions
router.get('/questions', async (req, res) => {
  try {
    const quizCollection = db.collection('alphabetQuizzes');
    const quizSnapshot = await quizCollection.get();
    let quizList = quizSnapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));
    
    quizList = shuffle(quizList);
    res.status(200).json(quizList);
  } catch (error) {
    res.status(500).json({ error: 'Error getting quiz questions' });
  }
});

// Submit Alphabet Quiz Answers
router.post('/submit', async (req, res) => {
  const { answers } = req.body;
  let score = 0;

  try {
    const answers = req.body.answers; // Array jawaban dari klien
    console.log('Received answers:', answers);

    const quizCollection = db.collection('alphabetQuizzes');
    const quizSnapshot = await quizCollection.get();
    const quizList = quizSnapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));
    console.log('Quiz list:', quizList);

    let score = 0;
    for (let i = 0; i < answers.length; i++) {
      const question = quizList.find(q => q.id === answers[i].id);
      console.log(`Comparing: ${question ? question.answer : 'undefined'} with ${answers[i].answer}`);
      if (question && question.answer === answers[i].answer) {
        score++;
      }
    }

    res.status(200).json({ score: score, total: quizList.length });
  } catch (error) {
    res.status(500).json({ error: 'Error submitting quiz answers' });
  }
});

module.exports = router;
