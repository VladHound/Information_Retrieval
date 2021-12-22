package com.kinoafisha;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

public class Search {
    private static final String INDEX_DIR = "C:\\Users\\vladp\\IdeaProjects\\Information Retrieval\\Index";

    public static void main(String[] args) throws IOException, ParseException {
        FSDirectory dir = new NIOFSDirectory(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        CustomAnalyzer analyzer = CustomAnalyzer.builder().withTokenizer("standard")
                .addTokenFilter("SnowballPorter", "language", "Russian")
                .addTokenFilter("lowercase")
                .addTokenFilter("stop")
                .build();
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Query lineQuery = new MultiFieldQueryParser(new String[]{
                "position", "title", "genres", "year", "countries", "rate", "link"
        }, analyzer).parse(line);
        TopDocs hits = searcher.search(lineQuery, 10);
        List<Document> list = new ArrayList<>();
        for (ScoreDoc score : hits.scoreDocs)
            list.add(searcher.doc(score.doc));
        int len = list.size();
        if (len != 0)
            for (int i = 0; i < len; i++)
                System.out.println(i + 1 + ") " +
                        list.get(i).get("position") + " | " +
                        list.get(i).get("title") + " | " +
                        list.get(i).get("genres") + " | " +
                        list.get(i).get("year") + " | " +
                        list.get(i).get("countries") + " | " +
                        list.get(i).get("rate") + " | " +
                        list.get(i).get("link"));
        else
            System.out.println("По данному запросу ничего не найдено");
    }
}
