package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

// 定义计算器类，继承自JFrame（窗口类）
class Calculator extends JFrame {
    // 声明私有成员变量
    private JPanel panel_top, panel_middle, panel_bottom; // 声明三个面板，分别用于顶部、中间和底部
    private JPanel secondTabPanel; // 第二个选项卡的面板
    private JTabbedPane tabbedPane; // 选项卡面板组件

    // 构造函数
    public Calculator() {
        // 初始化选项卡面板
        tabbedPane = new JTabbedPane();

        // 初始化计算器面板
        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout()); // 设置计算器面板的布局为边界布局

        // 初始化三个面板
        panel_top = new JPanel(); // 初始化顶部面板
        panel_middle = new JPanel(); // 初始化中间面板
        panel_bottom = new JPanel(); // 初始化底部面板

        // 初始化第二个选项卡的面板
        secondTabPanel = new JPanel();

        init(); // 调用初始化方法设置界面
    }

    public void init() {
        JPanel longPositionPanel = createLongPositionPanel(); // 创建做多面板（第一个选项卡）
        JPanel shortPositionPanel = createShortPositionPanel(); // 创建做空面板（第二个选项卡）

        tabbedPane.addTab("做多", longPositionPanel); // 添加第一个标签页
        tabbedPane.addTab("做空", shortPositionPanel); // 添加第二个标签页
        add(tabbedPane); // 将选项卡面板添加到JFrame中

        // 设置主窗口属性
        setBounds(300, 300, 320, 600); // 设置窗口位置和大小：x=300, y=300, 宽=320, 高=600
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置关闭窗口时退出程序
        setVisible(true); // 设置窗口可见
    }

    // 创建"做多"选项卡内容
    private JPanel createLongPositionPanel() {
        JPanel calculatorPanel = new JPanel(); // 创建计算器面板
        calculatorPanel.setLayout(new GridLayout(4, 1, 5, 5)); // 设置为4行1列网格布局，间距为5像素

        JPanel displayPanel = new JPanel(); // 创建显示面板
        displayPanel.setLayout(new BorderLayout(5, 0)); // 设置边界布局，水平间距为5像素

        // 文本框和 +/- 按钮的面板
        JPanel textWithButtonsPanel = new JPanel(new BorderLayout(5, 0));

        // 创建 - 按钮并放在左侧
        JButton minusButton = new JButton("-");
        minusButton.setPreferredSize(new Dimension(40, 30));
        textWithButtonsPanel.add(minusButton, BorderLayout.WEST);

        // 创建文本框并放在中间
        JTextField txt_result = new JTextField("1X"); // 创建文本框
        txt_result.setHorizontalAlignment(SwingConstants.CENTER); // 居中
        textWithButtonsPanel.add(txt_result, BorderLayout.CENTER); // 将文本框添加到中间

        // 创建 + 按钮并放在右侧
        JButton plusButton = new JButton("+");
        plusButton.setPreferredSize(new Dimension(40, 30));
        textWithButtonsPanel.add(plusButton, BorderLayout.EAST);

        // 将按钮和文本框组合添加到显示面板
        displayPanel.add(textWithButtonsPanel, BorderLayout.CENTER);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5)); // 设置边距

// 创建滑动条面板
        JPanel sliderPanel = new JPanel(new BorderLayout(5, 0));
// 创建滑动条，范围为1-100
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 100, 1);

// 设置滑动条的主刻度为25
        slider.setMajorTickSpacing(25);
        slider.setPaintTicks(true);      // 绘制刻度线

// 创建自定义标签
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("1X"));
        labelTable.put(25, new JLabel("25X"));
        labelTable.put(50, new JLabel("50X"));
        labelTable.put(75, new JLabel("75X"));
        labelTable.put(100, new JLabel("100X"));
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);     // 绘制刻度标签

// 不需要再单独显示1X和100X的标签，因为已经在刻度上显示了
        sliderPanel.add(slider, BorderLayout.CENTER);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5)); // 设置边距

        // 将滑动条添加到显示面板下方
        displayPanel.add(sliderPanel, BorderLayout.SOUTH);

        displayPanel.add(txt_result, BorderLayout.CENTER); // 将文本框添加到显示面板


/*
        calculatorPanel.setLayout(new BorderLayout()); // 设置为边界布局

        // 设置顶部面板属性
        panel_top.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); // 设置上下左右5像素的空白边框
        panel_top.setLayout(new BorderLayout()); // 设置顶部面板为边界布局
        JTextField txt_result = new JTextField("0"); // 创建文本框，初始显示"0"
        txt_result.setBackground(new Color(4, 189, 143)); // 设置文本框背景颜色为绿色
        txt_result.setHorizontalAlignment(SwingConstants.RIGHT); // 设置文本右对齐
        panel_top.add(txt_result); // 将文本框添加到顶部面板

        // 设置中间面板属性
        panel_middle.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); // 设置上下左右5像素的空白边框
        GridLayout gl = new GridLayout(4,4,2,2); // 创建4x4网格布局，行列间距为2像素
        panel_middle.setLayout(gl); // 设置中间面板为网格布局
        String[] str = {"7","8","9","+","4","5","6","-","1","2","3","*","0",".","=","/"}; // 按钮文本数组
        for (String i : str) {
            panel_middle.add(new JButton(i)); // 循环创建按钮并添加到中间面板
        }

        // 设置底部面板属性
        panel_bottom.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); // 设置上下左右5像素的空白边框
        panel_bottom.setLayout(new BorderLayout()); // 设置底部面板为边界布局
        panel_bottom.add(new JButton("CE/C")); // 添加清除按钮
        panel_bottom.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 设置黑色线条边框

        // 将三个面板添加到计算器面板中
        calculatorPanel.add(panel_top, BorderLayout.NORTH); // 顶部面板放在北边
        calculatorPanel.add(panel_middle, BorderLayout.CENTER); // 中间面板放在中央
        calculatorPanel.add(panel_bottom, BorderLayout.SOUTH); // 底部面板放在南边
*/
        // 第二行、第三行和第四行的面板（这里可以添加其他内容）
        JPanel secondRowPanel = new JPanel();
        JPanel thirdRowPanel = new JPanel();
        JPanel fourthRowPanel = new JPanel();

        // 将四个面板添加到计算器面板中
        calculatorPanel.add(displayPanel); // 添加显示面板
        calculatorPanel.add(secondRowPanel); // 添加第二行面板
        calculatorPanel.add(thirdRowPanel); // 添加第三行面板
        calculatorPanel.add(fourthRowPanel); // 添加第四行面板
        return calculatorPanel; // 返回完成配置的计算器面板
    }

    // 创建"做空"选项卡内容
    private JPanel createShortPositionPanel() {
        secondTabPanel.setLayout(new BorderLayout()); // 设置第二个选项卡面板为边界布局
        JLabel label = new JLabel("这是第二个选项卡", SwingConstants.CENTER); // 创建标签，居中对齐
        secondTabPanel.add(label, BorderLayout.CENTER); // 将标签添加到中央位置

        return secondTabPanel; // 返回完成配置的第二个选项卡面板
    }
}

public class Main {
    public static void main(String[] args) {
        new Calculator();
    }
}