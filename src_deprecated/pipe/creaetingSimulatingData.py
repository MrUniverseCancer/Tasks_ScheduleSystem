import pandas as pd
import numpy as np
import os
import sys

# ['LeftTime', 'ImportanceLevel', 'TaskConsuming', 'PunishLevel', 'PreferenceLevel', 'DifficultyLevel', 'BuuferbtwTasks', 'singalTaskTime']
# 对应0-8，分别表示剩余时长，重要程度，任务量（任务耗时），惩罚程度，喜好程度，困难程度，任务间缓冲时间，单个任务耗时

# Create a DataFrame
array_1 = np.array(["睡觉前补完数学作业", 2, 85, 1.2, 50, 80, 70, 0, 2, 90, 95]) # 睡觉前补完数学作业
array_2 = np.array(["补4节网课", 30, 60, 6, 20, 30, 70, 1, 1.5, 40, 60]) # 4天补完两周欠下的4*1.5小时的网课
array_3 = np.array(["购买生活物资", 18, 50, 0.5, 20, 80, 0, 0, 0.5, 40, 30]) # 3天内买水果、牛奶、卫生纸和洗衣液
array_4 = np.array(["GamePlaying", 15, 50, 3, 10, 90, 0, 0, 1, 30, 30]) # 2days Playing Games for 3 hours
array_5 = np.array(["Runing", 4, 80, 2, 0, 95, 80, 0, 2, 90, 60]) # runing every day
array_6 = np.array(["Reciting", 4, 80, 1, 0, 65, 80, 0, 1, 85, 60]) # reciting words every day
array_7 = np.array(["Python Project", 10, 40, 4, 80, 70, 50, 1, 2, 80, 80])
array_8 = np.array(["Read Book", 8, 30, 2, 70, 60, 40, 0, 2, 40, 50])
array_9 = np.array(["Cook Dinner", 5, 45, 1, 30, 40, 20, 0, 1, 50, 65])




array_Fact = np.array(["NULL", -1, -1, -1, -1, -1, -1, -1, -1, -1, -1])
for i in range(1, 10):
    array_Fact = np.vstack((array_Fact, eval("array" + "_" + str(i))))

Structure = pd.DataFrame(array_Fact, columns=['TaskName', 'LeftTime', 'ImportanceLevel', 'TaskConsuming', 
                                              'PunishLevel', 'PreferenceLevel', 'DifficultyLevel', 
                                              'BufferbtwTasks', 'singalTaskTime', 'FactImportance', 'FactUrgency'])



# 获取当前运行的 Python 脚本文件的绝对路径
script_path = os.path.abspath(sys.argv[0])
# 获取该脚本文件所在的目录路径
script_directory = os.path.dirname(script_path)
# 拼接要保存的 CSV 文件的完整路径
csv_file_path = os.path.join(script_directory, 'data.csv')
# 将 DataFrame 写入 CSV 文件
Structure.to_csv(csv_file_path, index=False)  # index=False 表示不保存行索引
