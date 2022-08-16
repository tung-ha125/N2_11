package DictionaryConsole;

public class Word {
    public String word_target;
    public String word_pronounce;
    public String word_explain;

    public Word() {
        word_target = "";
        word_pronounce = "";
        word_explain = "";
    }

    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_pronounce = "";
        this.word_explain = word_explain;
    }

    public Word(String word_target, String word_pronounce, String word_explain) {
        this.word_target = word_target;
        this.word_pronounce = word_pronounce;
        this.word_explain = word_explain;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public String getWord_target() {
        return word_target;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_pronounce() {
        return word_pronounce;
    }

    public void setWord_pronounce(String word_pronounce) {
        this.word_pronounce = word_pronounce;
    }

    public void printInfor() {
        System.out.println(word_target + " " + word_pronounce + ": " + word_explain);
    }
}
