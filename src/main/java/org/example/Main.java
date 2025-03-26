package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    public JTextField marginField; // 起始保证金显示
    public JTextField profitLossField; // 盈亏显示
    public JTextField returnRateField; // 回报率显示

    public Calculator() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super("合约计算器"); // 设置窗口标题
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("ToggleButton.background", Color.WHITE);
        UIManager.put("Button.opaque", Boolean.TRUE);
        UIManager.put("ToggleButton.opaque", Boolean.TRUE);
        contentPanel = new JPanel(new BorderLayout()); // 容器面板：NORTH多空切换按钮 CENTER功能面板
        setupSwitchButtons(); // 切换按钮面板
        setupCalculatorPanel(); // 功能面板
        init(); // 调用初始化方法设置界面
    }

    public void setupSwitchButtons() {
        JPanel topPanel = new JPanel(); //切换按钮面板
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5)); //流式布局 居中对齐 水平间距为0 垂直间距为5
        ButtonGroup buttonGroup = new ButtonGroup(); // 创建按钮组实现互斥

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
        // 创建计算器主面板，使用边界布局，组件间水平和垂直间距均为5像素
        calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout(5, 5));

        // ===== 杠杆设置区域 =====
        // 杠杆面板使用边界布局，组件间水平和垂直间距均为5像素
        JPanel leveragePanel = new JPanel(new BorderLayout(5, 5));

        // 创建包含文本框和+/-按钮的面板
        JPanel textWithButtonsPanel = new JPanel(new BorderLayout(5, 0));

        // 创建减号按钮，放在左侧
        JButton minusButton = new JButton("-");
        minusButton.setPreferredSize(new Dimension(45, 30)); // 设置按钮大小为45x30像素
        textWithButtonsPanel.add(minusButton, BorderLayout.WEST);

        // 创建显示杠杆值的文本框，放在中间
        txt_result = new JTextField("1x"); // 初始值为1x
        txt_result.setHorizontalAlignment(SwingConstants.CENTER); // 文本居中显示
        textWithButtonsPanel.add(txt_result, BorderLayout.CENTER);

        // 创建加号按钮，放在右侧
        JButton plusButton = new JButton("+");
        plusButton.setPreferredSize(new Dimension(45, 30)); // 设置按钮大小为45x30像素
        textWithButtonsPanel.add(plusButton, BorderLayout.EAST);

        // 将文本框和按钮组合面板添加到杠杆面板的顶部
        leveragePanel.add(textWithButtonsPanel, BorderLayout.NORTH);
        leveragePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5)); // 设置上下左右边距

        // ===== 滑动条区域 =====
        // 创建滑动条面板
        JPanel sliderPanel = new JPanel(new BorderLayout(5, 0));
        // 创建水平滑动条，范围1-100，初始值为1
        leverageSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 1);

        leverageSlider.setMajorTickSpacing(25); // 设置主刻度间隔为25
        leverageSlider.setPaintTicks(true);     // 显示刻度线

        // 创建自定义刻度标签
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("1x"));
        labelTable.put(25, new JLabel("25x"));
        labelTable.put(50, new JLabel("50x"));
        labelTable.put(75, new JLabel("75x"));
        labelTable.put(100, new JLabel("100x"));
        leverageSlider.setLabelTable(labelTable); // 设置标签表
        leverageSlider.setPaintLabels(true);      // 显示刻度标签

        // 添加滑动条值变化监听器，同步更新文本框显示
        leverageSlider.addChangeListener(_ -> {
            value = leverageSlider.getValue(); // 获取滑动条当前值
            txt_result.setText(value + "x");   // 更新文本框显示
        });

        // 减号按钮点击事件：减少杠杆值
        minusButton.addActionListener(_ -> {
            value--; // 杠杆值减1
            txt_result.setText(value + "x"); // 更新文本框
            leverageSlider.setValue(value);  // 同步更新滑动条位置
        });

        // 加号按钮点击事件：增加杠杆值
        plusButton.addActionListener(_ -> {
            this.value++; // 杠杆值加1
            txt_result.setText(value + "x"); // 更新文本框
            leverageSlider.setValue(value);  // 同步更新滑动条位置
        });

        // 文本框输入事件：直接修改杠杆值
        txt_result.addActionListener(_ -> {
            String text = txt_result.getText().replace("x", "").trim(); // 去除x字符并清除空格
            this.value = Integer.parseInt(text); // 解析为整数
            txt_result.setText(value + "x");     // 格式化显示
            leverageSlider.setValue(value);      // 同步滑动条
        });

        // 将滑动条添加到滑动条面板
        sliderPanel.add(leverageSlider, BorderLayout.CENTER);
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5)); // 设置边距
        leveragePanel.add(sliderPanel, BorderLayout.CENTER); // 将滑动条面板添加到杠杆面板

        // ===== 输入区域 =====
        // 创建价格和数量输入面板，使用网格包布局
        JPanel pricePanel = new JPanel(new GridBagLayout());
        pricePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10)); // 设置边距
        GridBagConstraints gbc = new GridBagConstraints(); // 创建布局约束对象
        gbc.fill = GridBagConstraints.HORIZONTAL; // 组件水平填充单元格
        gbc.insets = new Insets(5, 5, 5, 5);      // 设置组件间的间距

        // 开仓价格标签
        JLabel openPriceLabel = new JLabel("开仓价格");
        gbc.gridx = 0;      // 第一列
        gbc.gridy = 0;      // 第一行
        gbc.weightx = 0.3;  // 水平权重30%
        gbc.anchor = GridBagConstraints.WEST; // 左对齐
        pricePanel.add(openPriceLabel, gbc);

        // 创建开仓价格文本框和单位标签的组合面板
        JPanel openPriceWithUsdPanel = new JPanel(new BorderLayout(5, 0));
        openPriceField = new JTextField(""); // 创建文本框
        openPriceField.setPreferredSize(new Dimension(120, 25)); // 设置大小
        openPriceField.setHorizontalAlignment(SwingConstants.LEFT); // 文本右对齐
        openPriceWithUsdPanel.add(openPriceField, BorderLayout.CENTER);

        // USD单位标签
        JLabel openUsdLabel = new JLabel("USD");
        openPriceWithUsdPanel.add(openUsdLabel, BorderLayout.EAST);

        gbc.gridx = 0;     // 第一列
        gbc.gridy = 1;     // 第二行
        gbc.weightx = 0.7; // 水平权重70%
        pricePanel.add(openPriceWithUsdPanel, gbc);

        // 平仓价格标签
        JLabel closePriceLabel = new JLabel("平仓价格");
        gbc.gridx = 0;     // 第一列
        gbc.gridy = 2;     // 第三行
        gbc.weightx = 0.3; // 水平权重30%
        pricePanel.add(closePriceLabel, gbc);

        // 创建平仓价格文本框和单位标签的组合面板
        JPanel closePriceWithUsdPanel = new JPanel(new BorderLayout(5, 0));
        closePriceField = new JTextField(""); // 创建文本框
        closePriceField.setPreferredSize(new Dimension(120, 25)); // 设置大小
        closePriceField.setHorizontalAlignment(SwingConstants.LEFT); // 文本右对齐
        closePriceWithUsdPanel.add(closePriceField, BorderLayout.CENTER);

        // USD单位标签
        JLabel closeUsdLabel = new JLabel("USD");
        closePriceWithUsdPanel.add(closeUsdLabel, BorderLayout.EAST);

        gbc.gridx = 0;     // 第一列
        gbc.gridy = 3;     // 第四行
        gbc.weightx = 0.7; // 水平权重70%
        pricePanel.add(closePriceWithUsdPanel, gbc);

        // 成交数量标签
        JLabel quantityLabel = new JLabel("成交数量");
        gbc.gridx = 0;     // 第一列
        gbc.gridy = 4;     // 第五行
        gbc.weightx = 0.3; // 水平权重30%
        pricePanel.add(quantityLabel, gbc);

        // 成交数量文本框
        quantityField = new JTextField("");
        gbc.gridx = 0;     // 第一列
        gbc.gridy = 5;     // 第六行
        gbc.weightx = 0.7; // 水平权重70%
        quantityField.setPreferredSize(new Dimension(120, 25)); // 设置大小
        quantityField.setHorizontalAlignment(SwingConstants.LEFT); // 文本右对齐
        pricePanel.add(quantityField, gbc);

        // ===== 结果显示区域 =====
        // 创建结果显示面板，使用网格包布局
        JPanel resultsPanel = new JPanel(new GridBagLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("计算结果")); // 设置带标题的边框

        GridBagConstraints resultsGbc = new GridBagConstraints();
        resultsGbc.fill = GridBagConstraints.HORIZONTAL; // 组件水平填充
        resultsGbc.insets = new Insets(5, 10, 5, 10);    // 设置组件间距

        // 起始保证金行
        JLabel marginLabel = new JLabel("起始保证金");
        resultsGbc.gridx = 0;     // 第一列
        resultsGbc.gridy = 0;     // 第一行
        resultsGbc.weightx = 0.3; // 水平权重30%
        resultsGbc.anchor = GridBagConstraints.WEST; // 左对齐
        resultsPanel.add(marginLabel, resultsGbc);

        // 创建保证金显示文本框和单位标签的组合面板
        JPanel marginWithUnitPanel = new JPanel(new BorderLayout(5, 0));
        marginField = new JTextField();
        marginField.setEditable(false); // 设为不可编辑
        marginField.setHorizontalAlignment(SwingConstants.RIGHT); // 文本右对齐
        marginField.setBackground(new Color(240, 240, 240)); // 设置淡灰色背景
        marginWithUnitPanel.add(marginField, BorderLayout.CENTER);

        // USD单位标签
        JLabel marginUnitLabel = new JLabel("USD");
        marginWithUnitPanel.add(marginUnitLabel, BorderLayout.EAST);

        resultsGbc.gridx = 1;     // 第二列
        resultsGbc.gridy = 0;     // 第一行
        resultsGbc.weightx = 0.7; // 水平权重70%
        resultsPanel.add(marginWithUnitPanel, resultsGbc);

        // 盈亏行
        JLabel profitLossLabel = new JLabel("盈亏");
        resultsGbc.gridx = 0;     // 第一列
        resultsGbc.gridy = 1;     // 第二行
        resultsGbc.weightx = 0.3; // 水平权重30%
        resultsPanel.add(profitLossLabel, resultsGbc);

        // 创建盈亏显示文本框和单位标签的组合面板
        JPanel profitLossWithUnitPanel = new JPanel(new BorderLayout(5, 0));
        profitLossField = new JTextField();
        profitLossField.setEditable(false); // 设为不可编辑
        profitLossField.setHorizontalAlignment(SwingConstants.RIGHT); // 文本右对齐
        profitLossField.setBackground(new Color(240, 240, 240)); // 设置淡灰色背景
        profitLossWithUnitPanel.add(profitLossField, BorderLayout.CENTER);

        // USD单位标签
        JLabel profitLossUnitLabel = new JLabel("USD");
        profitLossWithUnitPanel.add(profitLossUnitLabel, BorderLayout.EAST);

        resultsGbc.gridx = 1;     // 第二列
        resultsGbc.gridy = 1;     // 第二行
        resultsGbc.weightx = 0.7; // 水平权重70%
        resultsPanel.add(profitLossWithUnitPanel, resultsGbc);

        // 回报率行
        JLabel returnRateLabel = new JLabel("回报率");
        resultsGbc.gridx = 0;     // 第一列
        resultsGbc.gridy = 2;     // 第三行
        resultsGbc.weightx = 0.3; // 水平权重30%
        resultsPanel.add(returnRateLabel, resultsGbc);

        // 创建回报率显示文本框和单位标签的组合面板
        JPanel returnRateWithUnitPanel = new JPanel(new BorderLayout(5, 0));
        returnRateField = new JTextField();
        returnRateField.setEditable(false); // 设为不可编辑
        returnRateField.setHorizontalAlignment(SwingConstants.RIGHT); // 文本右对齐
        returnRateField.setBackground(new Color(240, 240, 240)); // 设置淡灰色背景
        returnRateWithUnitPanel.add(returnRateField, BorderLayout.CENTER);

        // 百分比单位标签
        JLabel returnRateUnitLabel = new JLabel("%");
        returnRateWithUnitPanel.add(returnRateUnitLabel, BorderLayout.EAST);

        resultsGbc.gridx = 1;     // 第二列
        resultsGbc.gridy = 2;     // 第三行
        resultsGbc.weightx = 0.7; // 水平权重70%
        resultsPanel.add(returnRateWithUnitPanel, resultsGbc);

        // 将结果显示面板添加到计算器主面板的中央
        calculatorPanel.add(resultsPanel, BorderLayout.CENTER);

        // 添加一个位于底部的计算按钮，并改进其样式
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 创建一个使用居中流式布局的面板
        JButton calculateButton = new JButton("计算"); // 创建一个标签为"计算"的按钮
        calculateButton.setPreferredSize(new Dimension(250, 45)); // 设置按钮尺寸为宽200像素，高35像素
        calculateButton.setBackground(new Color(161, 133, 0)); // 设置按钮背景为暗黄色（无有效输入时）

        // 为输入字段添加文档监听器，用于检查输入是否有效
        DocumentListener inputListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { // 当文本插入时调用
                updateButtonColor(); // 更新按钮颜色
            }

            @Override
            public void removeUpdate(DocumentEvent e) { // 当文本删除时调用
                updateButtonColor(); // 更新按钮颜色
            }

            @Override
            public void changedUpdate(DocumentEvent e) { // 当文本属性改变时调用（主要用于样式文档）
                updateButtonColor(); // 更新按钮颜色
            }

            public void updateButtonColor() { // 私有方法，用于更新按钮颜色
                boolean hasValidInput = !openPriceField.getText().trim().isEmpty() && // 检查开仓价格字段是否非空
                        !closePriceField.getText().trim().isEmpty() && // 检查平仓价格字段是否非空
                        !quantityField.getText().trim().isEmpty(); // 检查数量字段是否非空

                if (hasValidInput) { // 如果所有字段都有内容
                    calculateButton.setBackground(Color.YELLOW); // 将按钮背景设置为明亮的黄色
                } else { // 如果有任何一个字段为空
                    calculateButton.setBackground(new Color(161, 133, 0)); // 将按钮背景设置为暗黄色
                }
            }
        };

        // 添加计算按钮的点击事件
        calculateButton.addActionListener(_ -> {
                double openPrice = Double.parseDouble(openPriceField.getText());
                double closePrice = Double.parseDouble(closePriceField.getText());
                double quantity = Double.parseDouble(quantityField.getText());

                // 计算保证金
                double margin = openPrice * quantity / value;

                // 计算盈亏
                double profitLoss;
                if (isLongPosition) {
                    // 做多：平仓价格 - 开仓价格
                    profitLoss = (closePrice - openPrice) * quantity;
                } else {
                    // 做空：开仓价格 - 平仓价格
                    profitLoss = (openPrice - closePrice) * quantity;
                }

                // 计算回报率
                double returnRate = (margin != 0) ? (profitLoss / margin * 100) : 0;

                // 显示结果，保留两位小数
                marginField.setText(String.format("%.2f", margin));
                profitLossField.setText(String.format("%.2f", profitLoss));
                returnRateField.setText(String.format("%.2f", returnRate));

                // 根据盈亏设置颜色
                if (profitLoss > 0) {
                    profitLossField.setForeground(new Color(0, 150, 0)); // 盈利为绿色
                } else {
                    profitLossField.setForeground(new Color(200, 0, 0)); // 亏损为红色
                }

                // 根据回报率设置颜色
                if (returnRate > 0) {
                    returnRateField.setForeground(new Color(0, 150, 0)); // 正回报为绿色
                } else {
                    returnRateField.setForeground(new Color(200, 0, 0)); // 负回报为红色
                }
        });

// 将监听器添加到每个输入字段的文档上
        openPriceField.getDocument().addDocumentListener(inputListener); // 监听开仓价格字段的变化
        closePriceField.getDocument().addDocumentListener(inputListener); // 监听平仓价格字段的变化
        quantityField.getDocument().addDocumentListener(inputListener); // 监听数量字段的变化
        buttonPanel.add(calculateButton); // 将按钮添加到按钮面板
        calculatorPanel.add(buttonPanel, BorderLayout.SOUTH);// 将按钮面板添加到主面板

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
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        new Calculator();
    }
}