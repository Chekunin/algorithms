# Segment tree data structure  
Структура данных, которая выполняет схожую задачу, что и Sparse Table. 
Она позволяет после некоторого препроцессинга быстро (за O(logN) time) выдавать ответ на запрос интервала массива, 
когда используется ассоциативная функция.  
То есть проедполоижм, что у нас есть массив **a** какого-то типа и ассоциативная функция **f**. Например, функция 
**f** может быть sum, multiplication, min, max, gcd и т.д.  

Наша задача:  
- Ответь на запрос интервала с левой границей **l** и правой границей **r**, то есть выполнить 
`f(a[l], a[l+1], ..., a[r-1], a[r])`.  
- Поддержать возможность замены элемента под каким-то индексом `a[index] = newItem`.  

Например, если у нас есть массив:  
`var a = [ 20, 3, -1, 101, 14, 29, 5, 61, 99 ]`  
И мы делаем запрос на интервал [3,7] с функцией суммы, то мы делаем следующее:  
`101 + 14 + 29 + 5 + 61 = 210`  
Такая прямой подход занимаем линейное **O(n)** время. Чтобы это можно было ускорить, можно использовать структуру 
данных Segment tree.  

## Структура Segment tree  
Segment tree - это просто бинарное дерево, где каждый узел является экземпляром класса `SegmentTree`:  
```
public class SegmentTree<T> {
  private var value: T
  private var function: (T, T) -> T
  private var leftBound: Int
  private var rightBound: Int
  private var leftChild: SegmentTree<T>?
  private var rightChild: SegmentTree<T>?
}
``` 
Каждый узел хранит в себе:  
- Индекс левой и правой границы интервала (leftBound и rightBound)  
- Указатели на дочерние узлы  (leftChild и rightChild) 
- Результат применения функции **f** для данного участка (value)  

Если у нас массив `[1, 2, 3, 4]` и функция `f = a + b`, то сегментное дерево будет выглядеть так:  
![](images/pict1.png)  
Здесь красным помечены границы интервалов для данного узла.  

## Построение Segment tree  
Вот как мы создаём узел дерева сегментов:  
```swift
public init(array: [T], leftBound: Int, rightBound: Int, function: @escaping (T, T) -> T) {
    self.leftBound = leftBound
    self.rightBound = rightBound
    self.function = function

    if leftBound == rightBound {
      value = array[leftBound]
    } else {
      let middle = (leftBound + rightBound) / 2

      leftChild = SegmentTree<T>(array: array, leftBound: leftBound, rightBound: middle, function: function)
      rightChild = SegmentTree<T>(array: array, leftBound: middle+1, rightBound: rightBound, function: function)

      value = function(leftChild!.value, rightChild!.value)
    }
}
```
Здесь корневому узлу мы даём полный массив с полным интервалом, после чего эта функция рекурсивно строит дерево.  

Операция построения дерева занимает **O(n)** time complexity.  

## Запрос на интервал массива  
```swift
public func query(withLeftBound: leftBound: Int, rightBound: Int) -> T {
    // 1
    if self.leftBound == leftBound && self.rightBound == rightBound {
      return self.value
    }

    guard let leftChild = leftChild else { fatalError("leftChild should not be nil") }
    guard let rightChild = rightChild else { fatalError("rightChild should not be nil") }

    // 2
    if leftChild.rightBound < leftBound {
      return rightChild.query(withLeftBound: leftBound, rightBound: rightBound)

    // 3
    } else if rightChild.leftBound > rightBound {
      return leftChild.query(withLeftBound: leftBound, rightBound: rightBound)

    // 4
    } else {
      let leftResult = leftChild.query(withLeftBound: leftBound, rightBound: leftChild.rightBound)
      let rightResult = rightChild.query(withLeftBound: rightChild.leftBound, rightBound: rightBound)
      return function(leftResult, rightResult)
    }
}
```
1. Сначала мы проверяем соответствует ли интервал данного узла запрошенному. Если да, то возвращаем значение данного 
узла.  
![](images/pict2.png)
2. Проверяем лежит ли запрошенный интервал полностью внутри правого узла.  
![](images/pict3.png)  
3.Проверяем лежит ли запрошенный интервал полностью внутри левого узла.  
![](images/pict4.png)  
4. Если ничего из вышеперечисленного, значит он лежит частично в интервале левого узла и в интервале правого узла.  
![](images/pict5.png)  

Запрос на интервал занимает *O(logN) time complexity*.  

## Замена элемента  
Значение узла Segment Tree зависит от значений его дочерних узлов. Поэтому если мы хотим изменить значение листового 
узла, то нам надо обновить все его родительские узлы.  
```swift
public func replaceItem(at index: Int, withItem item: T) {
    if leftBound == rightBound {
      value = item
    } else if let leftChild = leftChild, rightChild = rightChild {
      if leftChild.rightBound >= index {
        leftChild.replaceItem(at: index, withItem: item)
      } else {
        rightChild.replaceItem(at: index, withItem: item)
      }
      value = function(leftChild.value, rightChild.value)
    }
}
```
Замена элемента занимает **O(logN)** time complexity.  
