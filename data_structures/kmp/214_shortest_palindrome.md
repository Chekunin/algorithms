# 214. Shortest Palindrome [hard]  
Задача на [Leetcode.com](https://leetcode.com/problems/shortest-palindrome/).  

Данача строка `s`, нам надо преобразовать её в палиндром, мы можем добавлять символы только в начало строки.  
Задача: используя данное преобразование, вернуть самый короткий палиндром.  

**Пример 1:**  
```
Input: s = "aacecaaa"
Output: "aaacecaaa"
```

**Пример 2:**
```
Input: s = "abcd"
Output: "dcbabcd"
```

`s` состоит только из маленьких английских букв.  

## Решение  
Данную задачу мы можем решить при помощи КМП алгоритма.  
Создадим новую строку:
```java
String temp =  s + "#" + new StringBuilder(s).reverse().toString();
```
То есть возьмём исходную строку, добавим в ней разделяющий символ "#" (любой, который точно не встречается в `s`) и 
добавим реверсированую версию `s`.  
Дальше посчитаем для неё prefixTable (при помощи prefix-функции).  
Получится, что смое правое значение в prefixTable - это длина самого большого палиндрома из возможных префиксов.  
Значит нам остаётся в начало строки `s` добавить оставшиеся справа символы в обратном порядке.  
Это и будет ответ.  

```java
public class Solution {
    public String shortestPalindrome(String s) {
        String temp = s + "#" + new StringBuilder(s).reverse().toString();
        int[] table = getTable(temp);

        return new StringBuilder(s.substring(table[table.length - 1])).reverse().toString() + s;
    }

    public int[] getTable(String s){
        int[] table = new int[s.length()];

        int index = 0;
        for(int i = 1; i < s.length(); ){
            if(s.charAt(index) == s.charAt(i)){
                table[i] = ++index;
                i++;
            } else {
                if(index > 0){
                    index = table[index-1];
                } else {
                    index = 0;
                    i ++;
                }
            }
        }
        return table;
    }
}
```

Есть ещё решение, которое, использует rolling hash ([описание здесь](https://leetcode.com/problems/shortest-palindrome/discuss/60153/8-line-O(n)-method-using-Rabin-Karp-rolling-hash)):  
```java
class Solution {
    public String shortestPalindrome(String s) {
        int n = s.length(), pos = -1;
        long B = 26, MOD = 1000000007, POW = 1, hash1 = 0, hash2 = 0;
        for (int i = 0; i < n; i++, POW = POW * B % MOD) {
            hash1 = (hash1 * B + s.charAt(i) - 'a' + 1) % MOD;
            hash2 = (hash2 + (s.charAt(i) - 'a' + 1) * POW) % MOD;
            if (hash1 == hash2) pos = i;
        }
        return new StringBuilder().append(s.substring(pos + 1, n)).reverse().append(s).toString();
    }
}
```