import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/*
 * Решение задачи "Life Forms" [https://open.kattis.com/problems/lifeforms]
 * Задача: на вход подаётся число с количеством последующих строк.
 *         Каждая строка размером от 1 до 1 0006 алфовитных символов.
 *         Надо вывести список уникальынх подстрок, которые присутствуют больше чем в половине строках и размер которых
 *         максимальный. То есть в ответе все строки должны быть уникальными и одной длинны.
 * Например:
 * input:
 * 3
 * abcdefg
 * bcdefgh
 * cdefghi
 * ---------
 * output:
 * bcdefg
 * cdefgh
 * ===========
 * input:
 * 3
 * xxx
 * yyy
 * zzz
 * ---------
 * ? (means no common substring)
 */

class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            int count = Integer.parseInt(reader.readLine());
            if (count == 0) break;
            String[] strings = new String[count];
            for (int i = 0; i < count; i++) {
                strings[i] = reader.readLine();
            }
            LcsSolver solver = new LcsSolver(strings);
            System.out.println();
        }
    }

    public static class LcsSolver {
        // Inputs
        int k; // в скольких строках должна встречаться lcs
        int numSentinels, textLength;
        String[] strings;

        // Internal
        int shift; // на сколько будем смещать char из строк, чтобы привести к int (чтобы хватило разделителей)
        int lcsLen; // длинна longest common substring
        int lowestAsciiValue; // наименьший символ в общей строке (не считая разделительные символы)
        int highestAsciiValue; // наибольший символ в общей строке
        int[] imap; // ключ - индекс символа в общей строке, значение - номер цвета
        int[] text; // общая строка переведённая в числа
        int[] sa; // suffix array общей строки
        int[] lcp; // lcp array общей строки

        // Output
        TreeSet<String> lcss;

        public LcsSolver(String[] strings) {
            if (strings == null || strings.length <= 1)
                throw new IllegalArgumentException("Invalid strings array provided.");
            this.strings = strings;
        }

        private void init() {
            shift = lcsLen = 0;
            lowestAsciiValue = Integer.MAX_VALUE;
            highestAsciiValue = Integer.MIN_VALUE;
            numSentinels = strings.length;
            lcss = new TreeSet<>();
            imap = text = sa = lcp = null;

            computeTextLength(strings);
            buildReverseColorMapping();
            computeShift();
            buildText();
        }

        // высчитывает длину общей подстроки (вместе с разделителями)
        private void computeTextLength(String[] strings) {
            textLength = 0;
            for (String str : strings) textLength += str.length();
            textLength += numSentinels;
        }

        // заполняем imap, lowestAsciiValue и highestAsciiValue
        private void buildReverseColorMapping() {
            imap = new int[textLength];
            for (int i = 0, k = 0; i < strings.length; i++) {
                String str = strings[i];
                for (int j = 0; j < str.length(); j++) {
                    int asciiVal = str.charAt(j);
                    if (asciiVal < lowestAsciiValue) lowestAsciiValue = asciiVal;
                    if (asciiVal > highestAsciiValue) highestAsciiValue = asciiVal;
                    imap[k++] = i;
                }
                // Record that the sentinel belongs to string i
                imap[k++] = i;
            }
        }

        private void verifyMinAndMaxAsciiValues() {
            if (lowestAsciiValue == Integer.MAX_VALUE || highestAsciiValue == Integer.MIN_VALUE)
                throw new IllegalStateException("Must set min/max ascii values!");
        }

        private void computeShift() {
            verifyMinAndMaxAsciiValues();
            shift = numSentinels - lowestAsciiValue;
        }

        // Формируем общую подстроку. Мы должны заранее рассчитать lowest и highest ascii значения.
        // Все разделительные символы будут в интервале [0, numSentinels).
        // Все остальные значения символов будут в интервале [numSentinels, numSentinels + highestAsciiValue -
        // lowestAsciiValue]
        private void buildText() {
            verifyMinAndMaxAsciiValues();
            text = new int[textLength];
            int sentinel = 0;
            // Construct the new text with the shifted values and the sentinels
            for (int i = 0, k = 0; i < strings.length; i++) {
                String str = strings[i];
                for (int j = 0; j < str.length(); j++) {
                    text[k++] = ((int) str.charAt(j)) + shift;
                }
                text[k++] = sentinel++;
            }
        }

        public TreeSet<String> getLongestCommonSubstrings(int k) {
            if (k < 2) throw new IllegalArgumentException("k must be greater than or equal to 2");
            this.k = k;
            solve();
            return lcss;
        }

        // Считает кол-во суффиксов разного цвета между [lo, hi] и определяет достаточно ли их для LCS.
        private boolean enoughUniqueColorsInWindow(int lo, int hi) {
            // TODO(williamfiset): Avoid initializing a new hash set to count colors every method call.
            Set<Integer> set = new HashSet<>();
            for (int i = lo; i <= hi; i++) {
                set.add(imap[sa[i]]);
            }
            // TODO(williamfiset): Investigate if == can become >=
            return set.size() == k;
        }

        // возвращает Suffix array
        private static int[] getSa() {
            // не стал реализовывать метод,
            // здесь просто надо просто из int[] text создавать suffix array
        }

        // возвращает Longest Common Prefix array
        private static int[] getLcpArray() {
            // не стал реализовывать метод,
            // здесь просто надо просто из int[] sa создавать lcp
        }

        private void solve() {
            init();

            sa = getSa();
            lcp = getLcpArray();

            // TODO(williamfiset): Replace with SlidingWindowMinimum for speed.
            CompactMinSegmentTree tree = new CompactMinSegmentTree(lcp);

            int lo = numSentinels;
            int hi = numSentinels;

            while (true) {
                // сужаем окно (увеличивая lo) если в текущем интервале достаточно суффиксов разного цвета
                // или если hi достиг конца
                boolean shrinkWindow = hi == textLength - 1 || enoughUniqueColorsInWindow(lo, hi);

                if (shrinkWindow) {
                    lo++;
                } else {
                    hi++;
                }

                if (lo == textLength - 1) break;

                // Segment tree queries are right endpoint exclusive: [l, r)
                // so we must be careful to avoid the empty interval case.
                if (lo == hi) continue;

                int windowLcp = tree.query(lo + 1, hi + 1);
                addLcs(lo, hi, windowLcp);
            }
        }

        private void addLcs(int lo, int hi, int windowLcp) {
            if (windowLcp > lcsLen) {
                lcsLen = windowLcp;
                lcss.clear();
            }
            if (windowLcp == lcsLen) {
                lcss.add(retrieveString(sa[lo], windowLcp));
            }
        }

        // Возвращает строку из общей строки символов с указанной позицией и длинной
        // при этом делает обратное смещение (изначально мы смещали все символы)
        private String retrieveString(int i, int len) {
            char[] s = new char[len];
            for (int j = 0; j < len; j++) s[j] = (char) (text[i + j] - shift);
            return new String(s);
        }

        static class CompactMinSegmentTree {
            private int n;

            // Let UNIQUE be a value which does NOT and will NOT appear in the segment tree.
            private int UNIQUE = 93136074;

            // Segment tree values
            private int[] tree;

            public CompactMinSegmentTree(int size) {
                tree = new int[2 * (n = size)];
                java.util.Arrays.fill(tree, UNIQUE);
            }

            public CompactMinSegmentTree(int[] values) {
                this(values.length);
                for (int i = 0; i < n; i++) modify(i, values[i]);
            }

            // The segment tree function used for queries.
            private int function(int a, int b) {
                if (a == UNIQUE) return b;
                else if (b == UNIQUE) return a;
                return Math.min(a, b); // minimum value over a range
            }

            // Adjust point i by a value, O(log(n))
            public void modify(int i, int value) {
                tree[i + n] = function(tree[i + n], value);
                for (i += n; i > 1; i >>= 1) {
                    tree[i >> 1] = function(tree[i], tree[i ^ 1]);
                }
            }

            // Query interval [l, r), O(log(n))
            public int query(int l, int r) {
                int res = UNIQUE;
                for (l += n, r += n; l < r; l >>= 1, r >>= 1) {
                    if ((l & 1) != 0) res = function(res, tree[l++]);
                    if ((r & 1) != 0) res = function(res, tree[--r]);
                }
                if (res == UNIQUE) {
                    throw new IllegalStateException("UNIQUE should not be the return value.");
                }
                return res;
            }
        }
    }
}