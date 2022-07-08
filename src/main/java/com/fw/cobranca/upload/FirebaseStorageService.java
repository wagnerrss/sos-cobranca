package com.fw.cobranca.upload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class FirebaseStorageService {
    private Storage storage;
    static Bucket bucket;

    @PostConstruct
    private void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream in = FirebaseStorageService.class.getResourceAsStream("/serviceAccountKey.json");

            System.out.println(in);

            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build()
                    .getService();

            in = FirebaseStorageService.class.getResourceAsStream("/serviceAccountKey.json");

            FirebaseOptions
                    options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .setStorageBucket("sos-cobranca.appspot.com")
                    .setDatabaseUrl("https://sos-cobranca.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

            bucket = StorageClient.getInstance().bucket();

        }
    }

    public String upload(UploadInput uploadInput) {
        System.out.println(bucket);

//        Blob blob = bucket.create("nome.txt", "Wagner Souza".getBytes(), "text/html");

        byte[] bytes = Base64.getDecoder().decode(uploadInput.getBase64());

        String filename = uploadInput.getFilename();
        Blob blob = bucket.create(filename, bytes, uploadInput.getMimeType());

        //Assina URL válida por 30 dias
        //URL signedUrl = blob.signUrl(30, TimeUnit.DAYS);

        //Deixa URL pública
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), filename);
    }

    public Object delete(String documentPath) {
        System.out.println(bucket);

        System.out.println(documentPath);

        BlobId blobId = BlobId.of(bucket.getName(), documentPath);

        Object obj = storage.delete(blobId);

        System.out.println(obj);

        return obj;
    }
}
