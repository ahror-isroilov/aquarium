package task2;

public class Task2 {
    public static void main(String[] args) {
        double[] numbers = new double[]{215.41, 405.08  , 151.44, 147.22, 937.13, 239.91, 491.45, 521.17, 768.99, 613.84};
        double target = 143.5;
        search(numbers, 0, 0, target, "");
    }

    private static void search(double[] nums, int index, double sum, double target, String expression) {
        if (index == nums.length) {
            if (Math.abs(sum - target) < 1e-5) {
                System.out.println(expression + " = " + target);
            }
            return;
        }
        search(nums, index + 1, sum, target, expression);
        if (expression.isEmpty()) {
            search(nums, index + 1, sum + nums[index], target, Double.toString(nums[index]));
        } else {
            search(nums, index + 1, sum + nums[index], target, expression + " + " + nums[index]);
            search(nums, index + 1, sum - nums[index], target, expression + " - " + nums[index]);
        }
    }

}
