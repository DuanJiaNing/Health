package com.example.ai.forhealth.model.weight.bean;

/**
 *
 */
public class result {

    public String bmi;//BMI指数
    public String normbmi;//正常BMI指数
    public String idealweight;//理想体重
    public String level;//水平
    public String danger;//相关疾病发病的危险
    public String status;//是否正常

    @Override
    public String toString() {
        return "result{" +
                "bmi='" + bmi + '\'' +
                ", normbmi='" + normbmi + '\'' +
                ", idealweight='" + idealweight + '\'' +
                ", level='" + level + '\'' +
                ", danger='" + danger + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
//{
//        "status": "0",
//        "msg": "ok",
//        "result": {
    //        "bmi": "21.6",
    //        "normbmi": "18.5～23.9",
    //        "idealweight": "68",
    //        "level": "正常范围",
    //        "danger": "平均水平",
    //        "status": "1"
//        }
//        }