# NoBlack

## 问题

公司电脑必须设置屏保、屏保触发的时间很短，不能修改。

## 对策

使用jdk的 `Robot` 类的 `mouseMove` 函数，移动一下鼠标位置后立即回复到原始位置。

可以防止windows系统的屏保触发。

## 拓展功能

* 在windows任务托盘处添加图标，显示下次鼠标移动的时间。

* log4j记录log

* maven打包

# 参考

* [log4j](https://innovative.jp/archives/165)
