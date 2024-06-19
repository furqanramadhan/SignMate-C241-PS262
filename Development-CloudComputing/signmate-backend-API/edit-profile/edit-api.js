const express = require('express');
const router = express.Router();
const admin = require('firebase-admin');
const db = admin.firestore();

// Route untuk menampilkan profil pengguna
router.get('/profile', async (req, res) => {
  const email = req.query.email;

  try {
    const userSnapshot = await db.collection('users').where('email', '==', email).get();
    
    if (userSnapshot.empty) {
      return res.status(404).send({ message: 'User not found' });
    }

    const userData = userSnapshot.docs[0].data();
    res.status(200).send(userData);
  } catch (error) {
    res.status(500).send({ message: 'Error retrieving profile', error });
  }
});

// Route untuk mengedit profil pengguna
router.post('/edit', async (req, res) => {
  const { id, email, name, photo, gender, birthDate, phoneNumber } = req.body;

  try {
    const userRef = db.collection('users').doc(id);
    await userRef.update({
      email,
      name,
      photo,
      gender,
      birthDate,
      phoneNumber
    });

    res.status(200).send({ message: 'Profile updated successfully' });
  } catch (error) {
    res.status(500).send({ message: 'Error updating profile', error });
  }
});

module.exports = router;
