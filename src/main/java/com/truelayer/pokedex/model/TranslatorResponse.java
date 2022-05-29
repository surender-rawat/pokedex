package com.truelayer.pokedex.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslatorResponse {

    private Success success;
    private Contents contents;


    public static class Success{
        private int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    public static class Contents{

        private String translated;
        private String text;
        private String translation;

        public String getTranslated() {
            return translated;
        }

        public void setTranslated(String translated) {
            this.translated = translated;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }
    }
}
