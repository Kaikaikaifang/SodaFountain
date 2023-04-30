# SodaFountain

> Android 5.1
> friendlyarm

## 上位机 UI 与接口设计

1. mcu 接收颜色信息并向上位机发送信息，假设三种颜色为红绿蓝

   - 红：发送字符 `A`
   - 绿：发送字符 `B`
   - 蓝：发送字符 `C`

   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/256d2579-a02f-4149-9487-f98e7f921bc9/Untitled.png)

2. 上位机接收字符，并跳转至对应页面
   - `A` ：碳酸饮料
   - `B` ：果汁
   - `C` ：咖啡
3. 用户挑选饮料，上位机向 mcu 发送饮料名称

   `A`

   - `cola` : 可口可乐
   - `pepsi` : 百事可乐

   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0c156441-affb-4b3a-b87d-4eca12c47d03/Untitled.png)

   `B`

   - `strawberry` : 草莓汁
   - `grape` : 葡萄汁

   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7314d5dc-5dbd-4b11-81db-cbf7ad1227cf/Untitled.png)

   `C`

   - `mocha` : 摩卡
   - `latte` : 拿铁

   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/98faeb92-2940-4bbe-a970-a393bbbfc242/Untitled.png)

4. 接完饮料，mcu 向上位机发送结束信号字符 `E`

   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3b6bc1dd-7684-487c-a9dd-8563bc406387/Untitled.png)

5. 若 mcu 向上位机发送意料之外的信息，会返回当前所处的位置

   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/35e20399-eb6d-4e0b-8bd6-1c3b69d1fbf2/Untitled.png)

6. 为了便于 mcu 调试，首页与尾页按钮暂时均可点击，首页为跳转至碳酸饮料界面，尾页为返回首页
   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/538e854a-7756-4522-9b3c-081e00bd9f9b/Untitled.png)

   ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/37fee09e-ed70-4f1f-9b6b-51dd37165a14/Untitled.png)
