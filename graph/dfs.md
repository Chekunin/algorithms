# DFS
**Depth First Search (DFC)**  - алгоритм перебора или поиска по дереву или графу.
В случае графа он начинается с произвольной вершины, в случае дерева - с корня,
и идёт вглубь так далего, как только возможно до возврата к соседнему элементу.

Про DFS в целом можно почитать здесь:
* [techiedelight.com](https://www.techiedelight.com/depth-first-search/?__cf_chl_captcha_tk__=217f708b275f569cf83fb2ee383f83dbd153e208-1583051694-0-AbwSEnnymCZ9OpvbAR_yis-2rbF7H-9CnR9a1xqCyE4DLI7g07bpcB-F0PnCSIneIqQbCcKLAiQcQhPskNB7nq-LNKgBjSijfjOlDD67bV1aud6SrLcc_xmreONn6HL8_iYxJlL7hYlZtJH0DjTxMZa_vOWB_FdRV4RDjHfNKsel442eE8od-mY4IqkKKsqWh4pD0xkMDuogHWzfMqD1qQQ1gaDO44JrTTLjv5JkuNTy90iaxDhpXBy0PnHQG9_DWszTrushyZfMb2jYu03t-xKEpX-bc3L1KqANedob00g53rIIv_enIccIBn4xt6NAFx_4Rv-PTjtcgkWIpfgGAVJMV2NM3Xa7UeUrKuljMF8_VCsylEXrNqgma8BmjvTZV0YmfzijK8ikUJLCjY8_qXdKokdlac3tVAXR-tB7McSgzQ2K0TCX-POOvDrM6lKvx-JaBdibgLBtbBAsfKTGPTE)

## Recursive DFS n-ary tree
### Pre-ordered
```go
type TreeNode struct {
	Val int
	Children []*TreeNode
}

func DfsPreorder(root *TreeNode) {
	if root == nil {
		return
	}
	fmt.Println(root.Val)
	for _, v := range root.Children {
		DfsPreorder(v)
	}
}
```

### Post-ordered
```go
type TreeNode struct {
	Val int
	Children []*TreeNode
}

func DfsPostorder(root *TreeNode) {
	if root == nil {
		return
	}
	fmt.Println(root.Val)
	for _, v := range root.Children {
		DfsPostorder(v)
	}
}
```

### In-ordered
```go
type TreeNode struct {
	Val int
	Left *TreeNode
	Right *TreeNode
}

func DfsInorder(root *TreeNode) {
	if root == nil {
		return
	}
	DfsInorder(root.Left)
	fmt.Println(root.Val)
	DfsInorder(root.Right)
}
```

## Iterative DFS n-ary tree
Особого смысла в этом нет, так памяти тратится примерно столько же, сколько на рекурсивный способ.
Тк здесь мы используем сами стек узлов, а в рекурсивном способе используется стек вызовов функций,
а в реализации итеративный способ намного сложнее.
### Pre-ordered
```go
type TreeNode struct {
	Val int
	Children []*TreeNode
}

func DfsPreorder(root *TreeNode) {
	if root == nil {
		return
	}
	stack := []*TreeNode{}
	stack = append(stack, root)
	for len(stack) > 0 {
		curr := stack[len(stack)-1]
		stack = stack[:len(stack)-1]
		fmt.Println(curr.Val)
		// note: here we reverse an children's array
		// because of right child is pushed first so that left child
		// is precessed first (LIFO order)
		for i := len(curr.Children)-1; i >= 0; i-- {
			if curr.Children[i] != nil {
				stack = append(stack, curr.Children[i])
			}
		}
	}
}
```
Можно оптимизировать это решение, не добавляя в стек самого левого ребёнка.
Как это сделать описано [здесь](https://www.techiedelight.com/preorder-tree-traversal-iterative-recursive/?__cf_chl_captcha_tk__=91f00f6bdb4b218010a3e06373b9e2f3d296b92d-1582386458-0-AW7g-a4rG2XhPDz6tc6DwKRqPyYlapk7XhxRTdYGohGQ6_XJ-mzFwWAjyaR9FT3cE5AgYDoFtQYsxzTIfwgfuJMswA7nJ9N4EHIZdw78sA7L1LVpJjHs8d-CyJL8tYgQXZMNBV_JaLLKvunH6cnx-2-7fU4TyCex2t7ui6ROrg-Sz_ZZTtAEDWYtZ6PZcS3SdMGwHlnaBxlddpwE6-kHnGHKv8rOizu5G7pEGCLNoQAt7XSxg_rMvF5bTKusmd30320ggLQ_pU-deSjs4Jp7TlvLhr7jqJRSRayDKSP1gzaTybsgG5XWcp7Tixz6AagZxLPvqyVLpNq2gpyatbu0oH2GNf0XEw2yjxKPdajbI3S75kuy6TEusREb49_Xev3WKCFs1emohjUMuMjH0XC89pokGpexMKRzbkrU706aGZwR).
Алгоритм взял [отсюда](https://www.techiedelight.com/preorder-tree-traversal-iterative-recursive/)

### Post-ordered
```go
type TreeNode struct {
	Val int
	Children []*TreeNode
}

func DfsPreorder(root *TreeNode) {
	if root == nil {
		return
	}
	stack := []*TreeNode{}
	stack = append(stack, root)
	// another stack to store post-order traversal
	out := []int{}
	for len(stack) > 0 {
		curr := stack[len(stack)-1]
		stack = stack[:len(stack)-1]
		out = append(out, curr.Val)
		for _, v := range curr.Children {
			if v != nil {
				stack = append(stack, v)
			}
		}
	}
	// print post-order traversal
	for len(out) > 0 {
		val := out[len(out)-1]
		fmt.Println(val)
		out = out[:len(out)-1]
	}
}
```
Алгоритм взял [отсюда](https://www.techiedelight.com/postorder-tree-traversal-iterative-recursive)

### In-ordered
```go
type TreeNode struct {
	Val int
	Left *TreeNode
	Right *TreeNode
}

func DfsInorder(root *TreeNode) {
	if root == nil {
		return
	}
	stack := []*TreeNode{}
	curr := root
	// if current node is nil and stack is also empty, we're done
	for len(stack) > 0 || curr != nil {
		if curr != nil {
			stack = append(stack, curr)
			curr = curr.Left
		} else {
			curr = stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			fmt.Println(curr.Val)
			curr = curr.Right
		}
	}
}
```
Алгоритм взял [отсюда](https://www.techiedelight.com/inorder-tree-traversal-iterative-recursive/)

Задачи на DFS


# BFS
Когда применяется (для нахождения кратчайшего пути, тк с ним не прид>тся обходить все узлы, в отличие от DFS)
Iterative binary tree
Iterative n-ary tree (если конечно как-то отличается от binary)

Задачи на BFS


# Heap (priority queue)
Когда применяется
Ссылки на ресурсы, описывающие операции с ним
