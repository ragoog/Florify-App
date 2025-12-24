package Common;

import java.io.Serializable;

public class Post implements Serializable {

    public String username;
    public String content;

    public Post(String username, String content) {
        this.username = username;
        this.content = content;
    }

    @Override
    public String toString() {
        return username + " : " + content;
    }
}

