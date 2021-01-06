import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/*
* Решение задачи "Repeated Substrings" [https://open.kattis.com/problems/substrings]
* Задача: на вход подаётся число с количеством последующих строк.
*         Каждая строка не больше 100 000 алфовитных символов. Надо вывести кол-во повторяющхся больше 1 раза подстрок.
*         Например, если на вход пришла строка "aabaab", то ответ будет 5: “a”, “aa”, “aab”, “ab”, “b”.
*/

class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            System.out.println(repeatedCount(reader.readLine()));
        }
    }

    // принимает в вход строку и выдаёт кол-во уникальынх повторяющихся подстрок
    private static int repeatedCount(String str) {
        Suffix[] sa = buildSA(str);
        int[] lcp = buildLCP(str, sa);

        // делае подсчёт кол-ва повторяющися уникальных подстрок
        int res = 0;
        int prev = 0;
        for (int v : lcp) {
            if (v > prev) {
                res += v - prev;
            }
            prev = v;
        }
        return res;
    }

    // выдаёт Suffix Array
    private static Suffix[] buildSA(String str) {
        if (str == null || str.isEmpty()) return new Suffix[]{};
        int n = str.length();
        Suffix[] sa = new Suffix[n];
        for (int i = n-1; i >= 0; i--) {
            Suffix s = new Suffix(i, str.charAt(i), i < (n-1) ? sa[i+1].val : -1);
            sa[i] = s;
        }
        Arrays.sort(sa);

        int[] idx = new int[n];
        for (int length = 4; length < n*2; length <<= 1) {
            int prev = sa[0].val;
            sa[0].val = 0;
            idx[sa[0].idx] = 0;
            for (int i = 1; i < n; i++) {
                if (sa[i].val == prev && sa[i].next == sa[i-1].next) {
                    sa[i].val = sa[i-1].val;
                } else {
                    prev = sa[i].val;
                    sa[i].val = sa[i-1].val+1;
                }
                idx[sa[i].idx] = i;
            }
            for (int i = 0; i < n; i++) {
                int nextIdx = sa[i].idx + length/2;
                if (nextIdx < n) {
                    sa[i].next = sa[idx[nextIdx]].val;
                } else {
                    sa[i].next = -1;
                }
            }
            Arrays.sort(sa);
        }
        return sa;
    }

    // Kasai algorithm (LCP array)
    private static int[] buildLCP(String str, Suffix[] sa) {
        int N = sa.length;
        int[] lcp = new int[N];
        int[] inv = new int[N];
        for (int i = 0; i < N; i++) inv[sa[i].idx] = i;
        int k = 0;
        for (int i = 0; i < N; i++) {
            if (inv[i] == 0) continue;
            int prevIdx = sa[inv[i]-1].idx;
            while (prevIdx+k < N && i+k < N && str.charAt(prevIdx+k) == str.charAt(i+k)) k++;
            lcp[inv[i]] = k;
            if (k > 0) k -= 1;
        }
        return lcp;
    }

    static class Suffix implements Comparable<Suffix> {
        int idx;
        int val;
        int next;
        public Suffix(int idx, int val, int next) {
            this.idx = idx;
            this.val = val;
            this.next = next;
        }
        public int compareTo(Suffix s) {
            if (this.val != s.val) return Integer.compare(this.val, s.val);
            return Integer.compare(this.next, s.next);
        }
    }
}