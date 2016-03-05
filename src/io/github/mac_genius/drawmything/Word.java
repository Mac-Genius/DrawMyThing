package io.github.mac_genius.drawmything;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mac on 3/2/2016.
 */
public class Word implements Comparable<Word> {
    private String word;
    private int[] searchMatches;
    private List<String> colors;

    public Word(String word, int[] searchMatches, List<String> colors) {
        this.word = word;
        this.searchMatches = searchMatches;
        this.colors = colors;
    }

    public Word(JsonObject object) {
        word = object.get("word").getAsString().toLowerCase();

        JsonObject array =object.get("CharMatch").getAsJsonObject();
        int[] matches = new int[word.length()];
        for (int i = 0; i < array.get("size").getAsInt(); i++) {
            matches[i] = array.get((i + 1) + "").getAsInt();
        }
        searchMatches = matches;

        JsonArray color = object.get("SearchTerms").getAsJsonArray();
        ArrayList<String> colors = new ArrayList<>();
        for (int i = 0; i < color.size(); i++) {
            colors.add(color.get(i).toString());
        }

        this.colors = colors;
    }

    public Word() {}

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int[] getSearchMatches() {
        return searchMatches;
    }

    public void setSearchMatches(int[] searchMatches) {
        this.searchMatches = searchMatches;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public JsonObject getJson() {
        JsonObject object = new JsonObject();
        object.addProperty("word", word);

        JsonObject matches = new JsonObject();

        int match = 0;
        for (int i : searchMatches) {
            matches.addProperty((match + 1) + "", i);
            match++;
        }
        matches.addProperty("size", match);
        object.add("CharMatch", matches);

        JsonArray colors = new JsonArray();
        for (String s : this.colors) {
            colors.add(s);
        }
        object.add("SearchTerms", colors);

        return object;
    }

    public int getRank() {
        int rank = 0;
        for (int i = 0; i < searchMatches.length; i++) {
            rank += ((i + 1) * searchMatches[i]);
        }
        return rank;
    }

    @Override
    public int compareTo(Word o) {
        int thisRank = getRank();
        int thatRank = o.getRank();

        if (thisRank < thatRank) {
            return -1;
        } else if (thisRank > thatRank) {
            return 1;
        } else {
            return this.word.compareTo(o.getWord());
        }
    }

    public boolean equals(Object word) {
        if (word instanceof Word) {
            return this.word.equals(((Word) word).getWord());
        } else {
            return false;
        }
    }
}
