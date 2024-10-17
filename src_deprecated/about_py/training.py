import numpy as np
import pandas as pd
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
import os
import sys

# 取出任务数据
# 获取当前运行的 Python 脚本文件的绝对路径
script_path = os.path.abspath(__file__)
# 获取该脚本文件所在的目录路径
script_directory = os.path.dirname(script_path)
script_directory = os.path.dirname(script_directory)
# 拼接要保存的 CSV 文件的完整路径
csv_file_path = os.path.join(script_directory, 'pipe\\data.csv')
df = pd.read_csv(csv_file_path)

# 提取特征和目标变量
X = df[[ 'LeftTime', 'ImportanceLevel', 'TaskConsuming', 
          'PunishLevel', 'PreferenceLevel', 'DifficultyLevel', 
          'BufferbtwTasks', 'singalTaskTime']]
y_importance = df["FactImportance"]
y_urgency = df["FactUrgency"]


# 将数据拆分为训练集和测试集
X_train, X_test, y_urgency_train, y_urgency_test = train_test_split(X, y_urgency, test_size=0.1, random_state=42)
_, _, y_importance_train, y_importance_test = train_test_split(X, y_importance, test_size=0.1, random_state=42)

# 创建和训练线性回归模型
urgency_model = LinearRegression()
urgency_model.fit(X_train, y_urgency_train)

importance_model = LinearRegression()
importance_model.fit(X_train, y_importance_train)

# 对测试集进行预测
y_urgency_pred = urgency_model.predict(X_test)
y_importance_pred = importance_model.predict(X_test)

# 计算均方误差
urgency_mse = mean_squared_error(y_urgency_test, y_urgency_pred)
importance_mse = mean_squared_error(y_importance_test, y_importance_pred)

print("urgency MSE:", urgency_mse)
print("Importance MSE:", importance_mse)

# 新任务数据
new_task = np.array([10, 50, 2, 10, 60, 30, 1, 1.5]).reshape(1, -1)
new_task_df = pd.DataFrame(new_task, columns=[ 'LeftTime', 'ImportanceLevel', 'TaskConsuming', 
                                            'PunishLevel', 'PreferenceLevel', 'DifficultyLevel', 
                                            'BufferbtwTasks', 'singalTaskTime'])
# 预测剩余时间总和重要程度总
predicted_urgency = urgency_model.predict(new_task)
predicted_importance = importance_model.predict(new_task)

print("Predicted urgency Total:", predicted_urgency[0])
print("Predicted Importance Total:", predicted_importance[0])
