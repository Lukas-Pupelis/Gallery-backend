CREATE TABLE TAG
(
    ID   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    NAME TEXT UNIQUE NOT NULL,
    CONSTRAINT PK_TAG PRIMARY KEY (ID)
);

CREATE TABLE PHOTO_TAG
(
    PHOTO_ID BIGINT NOT NULL,
    TAG_ID   BIGINT NOT NULL,
    CONSTRAINT PK_PHOTO_TAG PRIMARY KEY (PHOTO_ID, TAG_ID),
    CONSTRAINT FK_PHOTO FOREIGN KEY (PHOTO_ID) REFERENCES PHOTO(ID),
    CONSTRAINT FK_TAG FOREIGN KEY (TAG_ID) REFERENCES TAG(ID)
);