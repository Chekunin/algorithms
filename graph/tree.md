# Tree data structure
В теории графов __дерево__ - это связный ациклический граф (для не ориентированных графов) или связанный ациклический граф, в котором не более одного узла не имеют входящих ребер, а остальные узлы имеют строго по одному входящему узлу (для ориентированных графов).  
_Связность_ означает наличие путей между любой парой вершин. _Ацикличность_ - отсутствие циклов и то, что между парами вершин имеется только по одному пути.  
_Лес_ - упорядоченное множество упорядоченных деревьев.  
_Ориентированное (направленное) дерево_ — ацикличный орграф (ориентированный граф, не содержащий циклов), в котором только одна вершина имеет нулевую степень захода (в неё не ведут дуги), а все остальные вершины имеют степень захода 1 (в них ведёт ровно по одной дуге). Вершина с нулевой степенью захода называется корнем дерева, вершины с нулевой степенью исхода (из которых не исходит ни одна дуга) называются концевыми вершинами или листьями.  
Больше таких определений на [википедии](https://ru.wikipedia.org/wiki/%D0%94%D0%B5%D1%80%D0%B5%D0%B2%D0%BE_(%D1%82%D0%B5%D0%BE%D1%80%D0%B8%D1%8F_%D0%B3%D1%80%D0%B0%D1%84%D0%BE%D0%B2)).


Деревья относятся к нелинейным структурам данных, тк они не хранят данные в линейном виде, как, например, _linked list, queues или stack_.  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-1.jpg)  

__Высота дереве (height)__ - длина самого длинного пути до leaf.  
__Глубина (depth)__ - длина пути от текущего узла до root.  

## Binary tree  
В бинарном дереве каждый узел имеет не более двух дочерних элементов, которые называются _left child_ и _right child_.  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-2.jpg)  
```golang
type TreeNode struct {
	Val int
	Left *TreeNode
	Right *TreeNode
}
```
Его можно представить в виде массива, где нулевой элемент - корень дерева, а левый и правый дочерние элементы i-ого узла под индексами i\*2+1 и i\*2+2 соотвественно.
```golang
func arrToTreeNode(arr []int, i int) *TreeNode {
	node := &TreeNode{
		Val: arr[i],
	}
	if i*2+1 < len(arr) {
		node.Left = arrToTreeNode(arr, i*2+1)
	}
	if i*2+2 < len(arr) {
		node.Right = arrToTreeNode(arr,i*2+2)
	}
	return node
}
```
### Вставка элемента  
Если мы хотим вставить левый элемент к какому-то узлу, то мы проверяем есть ли у него уже левый элемент.  
Если нет, просто добавляет новый элемент в Left.  
Если есть, то текущий левый элемент подставляем в левый элемент узла, который хотим вставить, и потом новый элемент подставляем куда хотели.  

### Depth-First Search (DFS)  
DFS - это алгоритм обхода или поиска по древовидной структуре данных. Он начиная с корня исследует как можно дальше вглубь каждой ветки перед теперь как переходить к следующей.  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-3.jpg)  
Результат этого слгоритма будет следущим: 1-2-3-4-5-6-7.  
Теперь, когда мы уже знакомы с этим алгоритма обхода, рассмотрим его типа: _pre-order, in-order и post-order_.  
#### Pre-order
Это именно то, что мы делали выше:  
1. Выводим значение текущего узла  
2. Переходим к левому узлу и выводим его. Только если левый узел существует.
3. Переходим к правому узлу и выводим его. Только если правый узел существует.
```golang
func (node *TreeNode) DFSPreOrder() {
	fmt.Println(node.Val)
	if node.Left != nil {
		node.Left.DFSPreOrder()
	}
	if node.Right != nil {
		node.Right.DFSPreOrder()
	}
}
```
#### In-order
У него результат был бы такой: 3–2–4–1–6–5–7.  
```golang
func (node *TreeNode) DFSInOrder() {
	if node.Left != nil {
		node.Left.DFSInOrder()
	}
	fmt.Println(node.Val)
	if node.Right != nil {
		node.Right.DFSInOrder()
	}
}
```
#### Post-order
У него результат был бы такой: 3–4–2–6–7–5–1.  
```golang
func (node *TreeNode) DFSPostOrder() {
	if node.Left != nil {
		node.Left.DFSPostOrder()
	}
	if node.Right != nil {
		node.Right.DFSPostOrder()
	}
	fmt.Println(node.Val)
}
```

### Breadth-First Search (BFS)
BFS - это алгоритм обхода или поиска по древовидной структуре данных. Он начиная с корня исследует сначала соседние узлы с того же уровня, прежде чем уйти на уровень ниже.  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-4.jpg)  
Рассмотрим пример:  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-5.jpg)  
Результат будет такой: 1–2–5–3–4–6–7.  
```golang
func (node *TreeNode) BFS() {
	// кладём node в очередь
	// дальше, пока очередь не пуста делаем:
	// достаём node из очереди
	// выводим его результат
	// если у этого node есть левый узел, кладём его в очередь
	// если есть правый, то кладём в очередь и его
	queue := make([]*TreeNode, 0)
	queue = append(queue, node)
	for len(queue) > 0 {
		currNode := queue[0]
		queue = queue[1:]
		fmt.Println(currNode.Val)
		if currNode.Left != nil {
			queue = append(queue, currNode.Left)
		}
		if currNode.Right != nil {
			queue = append(queue, currNode.Right)
		}
	}
}
```
Здесь мы использовали структуру данных _Queue_, вот как это работает:  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-6.jpg)  

## Binary Search tree
__Бинарное дерево поиска (Binary Search tree)__, иногда называемое __сортированным двоичным переводи (sorted binary trees)__, содержит свои значения в отсортированном виде, чтобы поиск и другие операции могли работать по принципу бинарного поиска.  
У него значение каждого узла больше или равно всего его левых потомок и меньше всех парвых потомков.  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-7.jpg)  
В примере выше дерево __А__ инвертировано, поэтому не является Binary Search tree.  
Дерево __B__ корректное (является Binary Search tree).  
Дерево __C__ не является Binary Search tree, у него проблема с узлом со значением 4. Оно должно быть слева от корня, тк как меньше его.  
Рассмотрим код по вставке нового узла, поиска по значению, удаления узла и проверка на баланс (является ли оно Binary Search tree).  
### Добавление нового элемента  
```golang
func (node *TreeNode) insertNode(val int) {
	if val <= node.Val {
		if node.Left != nil {
			node.Left.insertNode(val)
		} else {
			node.Left = &TreeNode{Val: val}
		}
	} else {
		if node.Right != nil {
			node.Right.insertNode(val)
		} else {
			node.Right = &TreeNode{Val: val}
		}
	}
}
```
### Поиск элемента  
```golang
func (node *TreeNode) isValInTree(val int) bool {
	if val < node.Val && node.Left != nil {
		return node.Left.isValInTree(val)
	}
	if val > node.Val && node.Right != nil {
		return node.Right.isValInTree(val)
	}
	return node.Val == val
}
```
```golang
func (node *TreeNode) getNode(val int) *TreeNode {
	if val < node.Val && node.Left != nil {
		return node.Left.getNode(val)
	}
	if val > node.Val && node.Right != nil {
		return node.Right.getNode(val)
	}
	if node.Val == val {
		return node
	} else {
		return nil
	}
}
```
### Удаление и организация
```golang
func (node *TreeNode) removeNode(val int, parentNode *TreeNode) bool {
	if val < node.Val && node.Left != nil {
		node.Left.removeNode(val, node)
	} else if val < node.Val {
		return false
	} else if val > node.Val && node.Right != nil {
		node.Right.removeNode(val, node)
	} else if val > node.Val {
		return false
	} else {
		// нашли наш элемент, рассматриваем случаи по удалению
		if node.Left == nil && node.Right == nil && node == parentNode.Left {
			parentNode.Left = nil
		} else if node.Left == nil && node.Right == nil && node == parentNode.Right {
			parentNode.Right = nil
		} else if node.Left != nil && node.Right == nil && node == parentNode.Left {
			parentNode.Left = node.Left
		} else if node.Left == nil && node.Right != nil && node == parentNode.Left {
			parentNode.Left = node.Right
		} else if node.Left != nil && node.Right == nil && node == parentNode.Right {
			parentNode.Right = node.Left
		} else if node.Left == nil && node.Right != nil && node == parentNode.Right {
			minNode := node.Right.findMinNode()
			parentNode.Right = minNode
		}
	}
	return true
}

func (node *TreeNode) findMinNode() *TreeNode {
	if node.Left != nil {
		return node.Left.findMinNode()
	} else {
		return node
	}
}
```

Делал конспект по [этой статье](https://www.freecodecamp.org/news/all-you-need-to-know-about-tree-data-structures-bceacb85490c/)

## Binary heap
__Двоичная куча (binary heap)__ - просто реализуемвя структура данных, позволяющая быстро (за логарифмическое время) добавлять элементы и извлекать элементы с максимальным приоритетом (например, максимальным по значению).  
Двоичная куча представляет собой полное бинарное дерево, для которого выполняется _основное свойство кучи_: приоритет каждой  вершины больше приоритетов её потомков. В простейшем случае приоритет каждой вершины можно считать равным её значению. В таком случае структура называется _max-heap_, польскольку корень поддерева является максимумом из значений элементов поддерева.  

_Дерево называется полным бинарным, если у каждой вершины есть не более двух потомков, а зполнение уровней вершин идёт сверху вниз (в пределах одного уровня - слева направо)._  
_Высота двоичной кучи равна высоте дерева, то есть log<sub>2</sub>N, где N - количество элементов массива._  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-8.jpg)  
### Добавление элемента
Новый элемент добавляется на последнее место в массиве (если рассматривать, что дерево представлено в виде массива).  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-9.jpg)  
Возможно, что при этом будет нарушено основное свойство кучи, тк новый элемент может быть в большем приоритете родителя. В таком случае следует "поднимать" новый элемент на один уровень (менять с вершиной-родителем) до тех пор, пока не будет соблюдено основное свойство кучи:  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-10.jpg)  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-11.jpg)  
Иначе говоря, новый элемент «всплывает», «проталкивается» вверх, пока не займет свое место. Сложность алгоритма не превышает высоты двоичной кучи (так как количество «подъемов» не больше высоты дерева), то есть равна O(log<sub>2</sub>N).  
```golang
func main() {
	arr := heap{20,15,11,6,9,7,8,1,3,5}
	arr.add(17)
	fmt.Println(arr) // [20 17 11 6 15 7 8 1 3 5 9]
}

type heap []int

func (h *heap) add(val int) {
	*h = append(*h, val)
	i := len(*h) - 1
	parent := (i - 1) / 2
	for i > 0 && (*h)[parent] < (*h)[i] {
		temp := (*h)[i]
		(*h)[i] = (*h)[parent]
		(*h)[parent] = temp
		i = parent
		parent = (i - 1) / 2
	}
}
```
### Упорядочение двоичной кучи
В ходе других операций с уже построенной двоичной кучей также может нарушиться основное свойство кучи, вершина может стать меньше своего потомка.  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-12.jpg)  
Метод __heapify__ восстанавливает основное свойство кучи для дерева с корнем в i-ой вершине при услоии, что оба поддерева ему удовлетворяют. Для этого необходимо "опускать" i-ую вершину (менять местами с наибольшим из потомков), пока основное свойство не будет восстановлено (процесс завершится, когда не найдётся потомка, большего своего родителя).  
Сложность этого алгоритма равна O(log<sub>2</sub>N).
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-13.jpg)  
![](https://github.com/Chekunin/algorithms/blob/master/graph/tree-14.jpg)  
```golang
// на вход принимаем индекс элемента, с которого сортируем
func (h *heap) heapify(i int) {
	var left int
	var right int
	var largest int
	for {
		left = i * 2 + 1
		right = i * 2 + 2
		largest = i
		if left < len(*h) && (*h)[left] > (*h)[largest] {
			largest = left
		}
		if right < len(*h) && (*h)[right] > (*h)[largest] {
			largest = right
		}
		if i == largest { break }
		temp := (*h)[i]
		(*h)[i] = (*h)[largest]
		(*h)[largest] = temp
		i = largest
	}
}
```
### Построение двоичной кучи  
Наиболее очевидный способ построить кучу из неупорядоченного массива - это по очереди добавить все его элементы.  
Временная оценка такого алгоритма O(N\*log<sub>2</sub>N).  
Однако можно построить кучу ещё быстрее - за O(N). Сначала следует построить дерево из всех элементов массива, не заботясь о соблюдении основного свойства кучи, а потом вызывать метод _heapify_ для всех вершин, у которых есть хотя бы один потомок (тк поддеревья, состоящие из одной вершины без потомков, уже порядочены). Потомки гарантированного есть у первых _heapsize/2_ вершин.  
```golang
func (h *heap) buildHead() {
	for i := len(*h)/2; i >= 0; i-- {
		h.heapify(i);
	}
}
```
### Извлечение (удаление) максимального элемента
В упорядоченном max-heap максимальный элемент всегда хранится в корне. Восстановить упорядоченность двоичной кучи после удаления максимального элемента можно, поставив на его место последний элемент и вызвав _heapify_ для корня, то есть упорядочив всё дерево.  
```golang
func (h *heap) getMax() int {
	result := (*h)[0]
	(*h)[0] = (*h)[len(*h)-1]
	*h = (*h)[:len(*h)-1]
	return result
}
```
### Сортировка с применением двоичной кучи
Можно отсортировать массив, сначала построив из него двоичную кучу, а потом последовательно извлекая максимальные элементы.  
Оценим временную сложность такого элемента: построение кучи - O(N), извлечение N элементов - O(N\*log<sub>2</sub>N).  
Следовательно, итоговая оценка O(N\*log<sub>2</sub>N). При этом дополнительная память для массива не используется.  
```golang
```

Таким образом, двоичная куча имеет структуру дерева логарифмической высоты (относительно количества вершин), позволяет за логарифмическое же время добавлять элементы и извлекать элемент с максимальным приоритетом за константное время. В то же время двоичная куча проста в реализации и не требует дополнительной памяти.

По Binary heap делал конспект по этой [статье](https://habr.com/ru/post/112222/)  
Также есть реализация IntHeap и PriorityQueue в Golang [здесь](https://golang.org/pkg/container/heap/)  
Разобраться с реализацией [heap sort](https://www.google.com/search?q=heap+sort+golan&oq=heap+sort+golan&aqs=chrome..69i57j0.2173j0j7&sourceid=chrome&ie=UTF-8)



## AVL tree
