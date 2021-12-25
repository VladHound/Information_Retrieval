package com.kinoafisha;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

public class Search {
    private static final String INDEX_DIR = "C:\\Users\\vladp\\IdeaProjects\\Information Retrieval\\Index";

    public static void main(String[] args) throws IOException, ParseException {
        FSDirectory dir = new NIOFSDirectory(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        CustomAnalyzer analyzer = CustomAnalyzer.builder()
                .withTokenizer("standard")
                .addTokenFilter("SnowballPorter", "language", "Russian")
                .addTokenFilter("lowercase")
                .addTokenFilter("stop")
                .addTokenFilter("porterstem")
                .addTokenFilter("capitalization")
                .build();
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        QueryParser qp = new MultiFieldQueryParser(new String[]{
                "position", "title", "genres", "year", "countries", "rate"
        }, analyzer);
        Query lineQuery = qp.parse(line);
        TopDocs hits = searcher.search(lineQuery, 1000);
        List<Document> list = new ArrayList<>();
        List<Document> list1 = new ArrayList<>();
        List<Document> list2 = new ArrayList<>();
        List<Document> list3 = new ArrayList<>();
        for (ScoreDoc score : hits.scoreDocs)
            list1.add(searcher.doc(score.doc));
        if (list1.size() != 0)
            for (Document indexableFields : list1) {
                if (indexableFields.get("genres").contains(line))
                    list2.add(indexableFields);
                for (String word : indexableFields.get("title").split(" "))
                    if (word.equals(line))
                        list3.add(indexableFields);
            }
        list.addAll(list3);
        list.addAll(list2);
        list.addAll(list1);
        if (list.size() != 0) {
            List<Document> deDupList = list.stream().distinct().collect(Collectors.toList());
            for (int i = 0; i < 10; i++)
                System.out.println(i + 1 + ") " +
                        deDupList.get(i).get("position") + " | " +
                        deDupList.get(i).get("title") + " | " +
                        deDupList.get(i).get("genres") + " | " +
                        deDupList.get(i).get("year") + " | " +
                        deDupList.get(i).get("countries") + " | " +
                        deDupList.get(i).get("rate") + " | " +
                        deDupList.get(i).get("link"));
        }
        else
            System.out.println("По данному запросу ничего не найдено");
    }
}
