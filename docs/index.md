---
layout: page
title: Mazes
image_sliders:
  - slider_grids
---

Based on *Mazes for Programmers* by Jamis Buck.


## Grids

Currently implemented are the default cartesian grid, a polar, a hex, a triangle, and an upsilon grid.

{% include slider.html selector="slider_grids" %}


## Algorithms

### Binary Tree

The first algorithm in the book is *BinaryTree*. The pattern is obvious, even with the variants on other grids.

{% include slider.html selector="slider_binaryTree" %}


### Aldous Broder

Aldous Broder is an algorithm which could generate all possible mazes.

{% include slider.html selector="slider_aldousBroder" %}


### Wilsons

Wilsons is one other algorithm which could generate all patterns.

{% include slider.html selector="slider_wilsons" %}


### Hunt And Kill

{% include slider.html selector="slider_huntAndKill" %}


### Recursive Backtracker

{% include slider.html selector="slider_recursiveBacktracker" %}


### Growing Tree

The algorithm *Growing Tree* is a parameterized implementation of other algorithms and can be used to mix the characteristics of different algorithms.

{% include slider.html selector="slider_growingTree" %}


## Credits

- [Mazes for Programmers](https://pragprog.com/titles/jbmaze/mazes-for-programmers/) by Jamis Buck
- [Jekyll Ideal Image Slider Include](https://github.com/jekylltools/jekyll-ideal-image-slider-include)
- [Image Slider](https://github.com/Codeinwp/Ideal-Image-Slider-JS)
