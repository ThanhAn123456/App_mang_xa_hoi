package Util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseUtil {
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public static DocumentReference currentUserDetail(){
        return FirebaseFirestore.getInstance().collection("User").document(currentUserId());
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("User");
    }
    public static CollectionReference allMessCollectionReference(){
        return FirebaseFirestore.getInstance().collection("Messages");
    }
}
