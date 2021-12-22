package com.kinoafisha;

import java.lang.reflect.Type;
import java.util.List;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.Reader;
import java.io.FileReader;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import com.google.gson.annotations.SerializedName;

public class Index {
    private static final Type Fields = new TypeToken<List<Fields>>() {
    }.getType();

    private static final String INDEX_DIR = "C:\\Users\\vladp\\IdeaProjects\\Information Retrieval\\Index";

    private static final String JSON_FILE = "C:\\Users\\vladp\\IdeaProjects\\Information Retrieval\\kinoafisha_data.json";

    public static void main(String[] args) throws IOException {
        Gson data = new Gson();
        FSDirectory dir = new NIOFSDirectory(Paths.get(INDEX_DIR));
        CustomAnalyzer analyzer = CustomAnalyzer.builder().withTokenizer("standard")
                .addTokenFilter("SnowballPorter", "language", "Russian")
                .addTokenFilter("lowercase").addTokenFilter("stop").build();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, config);
        Reader reader = new FileReader(JSON_FILE);
        try (reader) {
            List<Fields> list = data.fromJson(reader, Fields);
            for (Fields fields : list) {
                Document document = new Document();
                document.add(new TextField("position", fields.getPosition(), Field.Store.YES));
                document.add(new TextField("title", fields.getTitle(), Field.Store.YES));
                document.add(new TextField("genres", fields.getGenres(), Field.Store.YES));
                document.add(new TextField("year", fields.getYear(), Field.Store.YES));
                document.add(new TextField("countries", fields.getCountries(), Field.Store.YES));
                document.add(new TextField("rate", fields.getRate(), Field.Store.YES));
                document.add(new TextField("link", fields.getLink(), Field.Store.YES));
                writer.addDocument(document);
            }
        }
        writer.close();
    }
}

class Fields {
    @SerializedName("position")
    private String position;
    @SerializedName("title")
    private String title;
    @SerializedName("genres")
    private String genres;
    @SerializedName("year")
    private String year;
    @SerializedName("countries")
    private String countries;
    @SerializedName("rate")
    private String rate;
    @SerializedName("link")
    private String link;

    @Override
    public String toString() {
        return "Fields{position='" + position + "', title='" + title + "', genres='" + genres + "', year='" + year +
                "', countries='" + countries + "', rate='" + rate + "', link='" + link + "'}";
    }

    public String getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public String getGenres() {
        return genres;
    }

    public String getYear() {
        return year;
    }

    public String getCountries() {
        return countries;
    }

    public String getRate() {
        return rate;
    }

    public String getLink() {
        return link;
    }
}
