package edu.epam.bookshop.constant;

public class ImageStoragePath {

    private ImageStoragePath() {

    }

    public static final String DEFAULT_AVATAR_PATH = "http://localhost:8090/image/avatar/user_icon.png";
    public static final String AVATARS_DIRECTORY_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\image\\avatar";

    public static final String DEFAULT_AUTHOR_IMAGE_PATH = "http://localhost:8090/image/author/default-author.jpg";
    public static final String AUTHORS_DIRECTORY_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\image\\author";

    public static final String DEFAULT_PUBLISHER_IMAGE_PATH = "http://localhost:8090/image/publisher/default.png";
    public static final String PUBLISHERS_DIRECTORY_PATH = "C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\image\\publisher";

    public static final String AVATARS_LOCALHOST_PATH = "http://localhost:8090/image/avatar/";
    public static final String AUTHORS_LOCALHOST_PATH = "http://localhost:8090/image/author/";
    public static final String PUBLISHER_LOCALHOST_PATH = "http://localhost:8090/image/publisher/";
}
