public class encapsulationLibrary {

    String title;
    String author;
    String publicationDate;

    encapsulationLibrary(String title,
            String author,
            String publicationDate) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public static void main(String[] args) {

        encapsulationLibrary l1 = new encapsulationLibrary("Brief History of Time", "Stephen Hawking", "2009-11-07");
        String author=l1.getAuthor();
        System.out.println("The author is "+author);

    }

}