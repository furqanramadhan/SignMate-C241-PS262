package com.capstone.signmate_c241_ps262.firestore

import android.content.Context
import android.widget.Toast
import com.capstone.signmate_c241_ps262.response.Profile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class FirestoreService(private val context: Context) {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun getUserProfileByEmail(email: String): Profile? {
        return try {
            val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
            val profile = querySnapshot.documents.firstOrNull()?.toObject(Profile::class.java)
            profile
        } catch (e: FirebaseFirestoreException) {
            showToast("Firestore exception: ${e.message}")
            null
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
            null
        }
    }

    suspend fun updateUserProfile(profile: Profile): Boolean {
        return try {
            usersCollection.document(profile.id).set(profile).await()
            true
        } catch (e: FirebaseFirestoreException) {
            showToast("Firestore exception: ${e.message}")
            false
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
            false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}