# Tree data structure
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
