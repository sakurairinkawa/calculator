package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

class Calculator extends JFrame {
    public final JPanel contentPanel;
    public JPanel calculatorPanel;
    public JToggleButton longButton;
    public JToggleButton shortButton;
    public JTextField txt_result;
    public JSlider leverageSlider;
    public JTextField openPriceField; //开仓价格文本框
    public JTextField closePriceField; //平仓价格文本框
    public JTextField quantityField; // 开仓数量文本框
    public boolean isLongPosition = true; // 默认做多状态
    public int value = 1; // 杠杆数值

    public Calculator() {
        super("合约计算器"); // 设置窗口标题
        contentPanel = new JPanel(new BorderLayout()); // 容器面板：NORTH多空切换按钮 CENTER功能面板
        setupSwitchButtons(); // 切换按钮面板
        setupCalculatorPanel(); // 功能面板
        init(); // 调用初始化方法设置界面
    }

    public void setupSwitchButtons() {
        JPanel topPanel = new JPanel(); //切换按钮面板
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5)); //流式布局 居中对齐 水平间距为0 垂直间距为5
        ButtonGroup buttonGroup = new ButtonGroup(); // 创建按钮组确保两个按钮互斥

        longButton = new JToggleButton("做多");
        longButton.setPreferredSize(new Dimension(120, 30));
        longButton.setSelected(true); // 默认选中

        shortButton = new JToggleButton("做空");
        shortButton.setPreferredSize(new Dimension(120, 30));

        // 添加到按钮组
        buttonGroup.add(longButton);
        buttonGroup.add(shortButton);

        // 添加切换事件
        longButton.addActionListener(_ -> {
            if (longButton.isSelected()) {
                isLongPosition = true;
            }
        });

        shortButton.addActionListener(_ -> {
            if (shortButton.isSelected()) {
                isLongPosition = false;
            }
        });

        // 将按钮添加到顶部面板
        topPanel.add(longButton);
        topPanel.add(shortButton);
        // 添加顶部面板到内容面板
        contentPanel.add(topPanel, BorderLayout.NORTH);
    }

    private void setupCalculatorPanel() {
        calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout(5, 5));

        // 杠杆滑动条部分
        JPanel leveragePanel = new JPanel(new BorderLayout(5, 5));

        // 文本框和 +/- 按钮的面板
        JPanel textWithButtonsPanel = new JPanel(new BorderLayout(5, 0));

        // 创建 - 按钮并放在左侧
        JButton minusButton = new JButton("-");
        minusButton.setPreferredSize(new Dimension(45, 30));
        textWithButtonsPanel.add(minusButton, BorderLayout.WEST);

        // 创建文本框并放在中间
        txt_result = new JTextField("1x"); // 普通文本框
        txt_result.setHorizontalAlignment(SwingConstants.CENTER); // 居中
        textWithButtonsPanel.add(txt_result, BorderLayout.CENTER); // 将文本框添加到中间

        // 创建 + 按钮并放在右侧
        JButton plusButton = new JButton("+");
        plusButton.setPreferredSize(new Dimension(45, 30));
        textWithButtonsPanel.add(plusButton, BorderLayout.EAST);

        // 将按钮和文本框组合添加到显示面板
        leveragePanel.add(textWithButtonsPanel, BorderLayout.NORTH);
        leveragePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5)); // 设置边距

        // 创建滑动条面板
        JPanel sliderPanel = new JPanel(new BorderLayout(5, 0));
        // 创建滑动条，范围为1-100
        leverageSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 1);

        leverageSlider.setMajorTickSpacing(25);// 设置滑动条的主刻度为25
        leverageSlider.setPaintTicks(true);// 绘制刻度线

        // 创建自定义标签
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("1x"));
        labelTable.put(25, new JLabel("25x"));
        labelTable.put(50, new JLabel("50x"));
        labelTable.put(75, new JLabel("75x"));
        labelTable.put(100, new JLabel("100x"));
        leverageSlider.setLabelTable(labelTable);
        leverageSlider.setPaintLabels(true);     // 绘制刻度标签

        // 添加滑动条值变化的监听器，可以同步更新文本框
        leverageSlider.addChangeListener(_ -> {
            value = leverageSlider.getValue();
            txt_result.setText(value + "x");
        });
        //-按钮的监听
        minusButton.addActionListener(_ -> {
            value--;
            txt_result.setText(value + "x");
            leverageSlider.setValue(value); // 同步更新滑动条的值

        });

        //+按钮的监听
        plusButton.addActionListener(_ -> {
            this.value++;
            txt_result.setText(value + "x");
            leverageSlider.setValue(value); // 同步更新滑动条的值

        });

        //文本框的监听
        txt_result.addActionListener(_ -> {
            String text = txt_result.getText().replace("x", "").trim();
            this.value = Integer.parseInt(text);
            txt_result.setText(value + "x");
            leverageSlider.setValue(value); // 同步滑动条

        });

        sliderPanel.add(leverageSlider, BorderLayout.CENTER);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5)); // 设置边距
        leveragePanel.add(sliderPanel, BorderLayout.CENTER);

        // 输入价格和数量的面板
        JPanel pricePanel = new JPanel(new GridBagLayout()); // 使用GridBagLayout布局管理器，可以精确控制组件位置和大小
        pricePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10)); // 设置面板边距
        GridBagConstraints gbc = new GridBagConstraints(); // 创建GridBagConstraints对象，用于设置组件的布局约束
        gbc.fill = GridBagConstraints.HORIZONTAL; // 设置组件水平填充其单元格空间
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件的外边距(上,左,下,右)

        // 开仓价格标签
        JLabel openPriceLabel = new JLabel("开仓价格"); // 创建标签
        gbc.gridx = 0; // 设置组件在网格的x坐标(第1列)
        gbc.gridy = 0; // 设置组件在网格的y坐标(第1行)
        gbc.weightx = 0.3; // 设置水平方向上分配额外空间的权重(30%)
        gbc.anchor = GridBagConstraints.WEST; // 设置组件在单元格内靠左对齐
        pricePanel.add(openPriceLabel, gbc); // 将标签添加到面板中并应用约束

        // 开仓价格文本框
        openPriceField = new JTextField(""); // 创建文本框
        gbc.gridx = 0; // 第一列
        gbc.gridy = 1; // 第二行
        gbc.weightx = 0.7; // 设置水平方向上分配额外空间的权重(70%)
        openPriceField.setPreferredSize(new Dimension(150, 25)); // 设置文本框的首选大小
        pricePanel.add(openPriceField, gbc); // 将文本框添加到面板中并应用约束

        // 平仓价格标签
        JLabel closePriceLabel = new JLabel("平仓价格"); // 创建标签
        gbc.gridx = 0; // 第一列
        gbc.gridy = 2; // 第三行
        gbc.weightx = 0.3; // 保持标签的水平权重为30%
        pricePanel.add(closePriceLabel, gbc); // 将标签添加到面板中并应用约束

        // 平仓价格文本框
        closePriceField = new JTextField(""); // 创建文本框
        gbc.gridx = 0; // 第一列
        gbc.gridy = 3; // 第四行
        gbc.weightx = 0.7; // 设置水平权重为70%
        closePriceField.setPreferredSize(new Dimension(150, 25)); // 设置首选大小
        pricePanel.add(closePriceField, gbc); // 将文本框添加到面板中并应用约束

        // 开仓数量标签
        JLabel quantityLabel = new JLabel("开仓数量"); // 创建标签
        gbc.gridx = 0; // 第一列
        gbc.gridy = 4; // 第五行
        gbc.weightx = 0.3; // 保持标签的水平权重为30%
        pricePanel.add(quantityLabel, gbc); // 将标签添加到面板中并应用约束

        // 开仓数量文本框
        quantityField = new JTextField(""); // 创建文本框
        gbc.gridx = 0; // 第一列
        gbc.gridy = 5; // 第六行
        gbc.weightx = 0.7; // 设置水平权重为70%
        quantityField.setPreferredSize(new Dimension(150, 25)); // 设置首选大小
        pricePanel.add(quantityField, gbc); // 将文本框添加到面板中并应用约束

        leveragePanel.add(pricePanel, BorderLayout.SOUTH);//添加滑动条到主面板
        calculatorPanel.add(leveragePanel, BorderLayout.NORTH);// 添加杠杆面板到主面板
        contentPanel.add(calculatorPanel, BorderLayout.CENTER);// 添加面板到contentPanel CENTER
        add(contentPanel);// 将内容面板添加到JFrame

    }

    private void init() {
        setBounds(300, 300, 320, 600); // 设置窗口位置和大小：x=300, y=300, 宽=320, 高=600
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭窗口时退出程序
        setVisible(true); // 窗口可见
    }
}

public class Main {
    public static void main(String[] args) {
        new Calculator();
    }
}