package cn.webfuse.ext.kit;

import cn.webfuse.framework.core.kit.NumberKits;

import java.util.*;

/**
 * 运算表达式
 * from http://elim.iteye.com/blog/1981197
 */
public class OperationExpressionKits {

    /**
     * 计算简单的运算表达式
     *
     * @param operationExpr
     * @return
     */
    public static double evaluate(String operationExpr) {
        //把表达式用运算符、括号分割成一段一段的，并且分割后的结果包含分隔符
        StringTokenizer tokenizer = new StringTokenizer(operationExpr, "+-*/()", true);

        Stack<Double> numStack = new Stack<>();   //用来存放数字的栈
        Stack<Operator> operatorStack = new Stack<>();  //存放操作符的栈

        Map<String, Operator> computeOperator = getComputeOperator();    //获取运算操作符
        String currentElement;  //当前元素

        while (tokenizer.hasMoreElements()) {
            currentElement = tokenizer.nextToken().trim();//去掉前后的空格
            if (!"".equals(currentElement)) { //只处理非空字符
                if (NumberKits.isNumber(currentElement)) { //为数字时则加入到数字栈中
                    numStack.push(NumberKits.parseNumber(currentElement, Double.class));
                } else {
                    Operator currentOperator = computeOperator.get(currentElement);//获取当前运算操作符
                    if (currentOperator != null) {//不为空时则为运算操作符
                        while (!operatorStack.empty() && operatorStack.peek().priority() >= currentOperator.priority()) {
                            compute(numStack, operatorStack);
                        }
                        //计算完后把当前操作符加入到操作栈中
                        operatorStack.push(currentOperator);
                    } else {//括号
                        if ("(".equals(currentElement)) { //左括号时加入括号操作符到栈顶
                            operatorStack.push(Operator.BRACKETS);
                        } else { //右括号时, 把左括号跟右括号之间剩余的运算符都执行了。
                            while (!operatorStack.peek().equals(Operator.BRACKETS)) {
                                compute(numStack, operatorStack);
                            }
                            operatorStack.pop();//移除栈顶的左括号
                        }
                    }
                }

            }

        }

        // 经过上面代码的遍历后最后的应该是nums里面剩两个数或三个数，operators里面剩一个或两个运算操作符
        while (!operatorStack.empty()) {
            compute(numStack, operatorStack);
        }
        return numStack.pop();

    }

    private static Map<String, Operator> getComputeOperator() {
        return new HashMap<String, Operator>() { // 运算符
            {
                put("+", Operator.PLUS);
                put("-", Operator.MINUS);
                put("*", Operator.MULTIPLY);
                put("/", Operator.DIVIDE);
            }
        };
    }

    /**
     * 取numStack的最顶上两个数字，operStack的最顶上一个运算符进行运算，然后把运算结果再放到numStack的最顶端
     *
     * @param numStack  数字栈
     * @param operStack 操作栈
     */
    private static void compute(Stack<Double> numStack, Stack<Operator> operStack) {
        Double num2 = numStack.pop(); // 弹出数字栈最顶上的数字作为运算的第二个数字
        Double num1 = numStack.pop(); // 弹出数字栈最顶上的数字作为运算的第一个数字
        Double computeResult = operStack.pop().compute(
                num1, num2); // 弹出操作栈最顶上的运算符进行计算
        numStack.push(computeResult); // 把计算结果重新放到队列的末端
    }

    /**
     * 运算符
     */
    private enum Operator {
        /**
         * 加
         */
        PLUS {
            @Override
            public int priority() {
                return 1;
            }

            @Override
            public double compute(double num1, double num2) {
                return num1 + num2;
            }
        },
        /**
         * 减
         */
        MINUS {
            @Override
            public int priority() {
                return 1;
            }

            @Override
            public double compute(double num1, double num2) {
                return num1 - num2;
            }
        },
        /**
         * 乘
         */
        MULTIPLY {
            @Override
            public int priority() {
                return 2;
            }

            @Override
            public double compute(double num1, double num2) {
                return num1 * num2;
            }
        },
        /**
         * 除
         */
        DIVIDE {
            @Override
            public int priority() {
                return 2;
            }

            @Override
            public double compute(double num1, double num2) {
                return num1 / num2;
            }
        },
        /**
         * 括号
         */
        BRACKETS {
            @Override
            public int priority() {
                return 0;
            }

            @Override
            public double compute(double num1, double num2) {
                return 0;
            }
        };

        /**
         * 对应的优先级
         *
         * @return
         */
        public abstract int priority();

        /**
         * 计算两个数对应的运算结果
         *
         * @param num1 第一个运算数
         * @param num2 第二个运算数
         * @return
         */
        public abstract double compute(double num1, double num2);
    }

//    public static void main(String[] args) {
//        String computeExpr = "1 + 5 * 6 + 3 * (2 + 3*2+2-1+3*3) + 10/5 - 6*1";
//        System.out.println(evaluate(computeExpr));
//    }

}
