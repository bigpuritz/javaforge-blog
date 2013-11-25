package net.javaforge.blog.camel.rnd;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Generates random string sequence.
 */
public enum RndGenerator {

    RANDOM {
        @Override
        public String generate() {
            if (chars != null)
                return RandomStringUtils.random(length, chars);
            else
                return RandomStringUtils.random(length, start, end, letters,
                        numbers);
        }
    },
    ALPHABETIC {
        @Override
        public String generate() {
            return RandomStringUtils.randomAlphabetic(length);
        }
    },
    ALPHANUMERIC {
        @Override
        public String generate() {
            return RandomStringUtils.randomAlphanumeric(length);
        }
    },
    NUMERIC {
        @Override
        public String generate() {
            return RandomStringUtils.randomNumeric(length);
        }
    },
    ASCII {
        @Override
        public String generate() {
            return RandomStringUtils.randomAscii(length);
        }
    };

    int length;

    String chars;

    boolean letters = false;

    boolean numbers = false;

    int start = 0;

    int end = 0;

    private RndGenerator() {
        this(10);
    }

    private RndGenerator(int length) {
        this.length = length;
    }

    public RndGenerator chars(String chars) {
        this.chars = chars;
        return this;
    }

    public RndGenerator letters(boolean letters) {
        this.letters = letters;
        return this;
    }

    public RndGenerator numbers(boolean numbers) {
        this.numbers = numbers;
        return this;
    }

    public RndGenerator length(int length) {
        this.length = length;
        return this;
    }

    public RndGenerator start(int start) {
        this.start = start;
        return this;
    }

    public RndGenerator end(int end) {
        this.end = end;
        return this;
    }

    /**
     * Generates random string sequence...
     *
     * @return random string
     */
    public abstract String generate();

}
