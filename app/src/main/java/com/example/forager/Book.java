package com.example.forager;

public class Book {
    private String title;
    private String[] authors;
    private int numOfAuthors;
    private String description;
    private String smallThumbnailLink;

    public Book(String title, String[] authors, int numOfAuthors, String description, String smallThumbnailLink) {
        this.title = title;
        this.authors = authors;
        this.numOfAuthors = numOfAuthors;
        this.description = description;
        this.smallThumbnailLink = smallThumbnailLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getAuthors() {
        String Authors = "";
        for (int i = 0; i < this.getNumOfAuthors(); i++) {
            Authors += "[ " + authors[i] + " ] ";
        }
        return Authors;
    }

    public void setAuthors(String[] value) {
        this.authors = value;
    }

    public int getNumOfAuthors() {
        return numOfAuthors;
    }

    public void setNumOfAuthors(int value) {
        this.numOfAuthors = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getSmallThumbnailLink() {
        return smallThumbnailLink;
    }

    public void setSmallThumbnailLink(String value) {
        this.smallThumbnailLink = value;
    }

    public void showBook() {
        System.out.println(getTitle() + "\n");
        for (int i = 0; i < numOfAuthors; i++) {
            System.out.println(authors[i] + "\n");
        }
        System.out.println(getDescription() + "\n");
        System.out.println(getSmallThumbnailLink() + "\n");
    }

    public String returnResults() {
        String authorsString = "";
        for (int i = 0; i < numOfAuthors; i++) {
            authorsString += authors[i] + "\n";
        }

        String results = "";
        results += "Title: " + this.getTitle() + "\n\n" +
                "\n\n" + "Author(s): " + authorsString + "\n\n" +
                this.getDescription() + "\n";
        if (results.length() == 0) {
            return "String Empty.";
        }
        return results;
    }

}

