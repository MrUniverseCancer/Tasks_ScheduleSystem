import pandas as pd
# import numpy as np
import os
import sys

# warning! 请在当前目录下面运行文件
sys.path.append("..")
import about_py.task_struct as task_struct

# 获取当前运行的 Python 脚本文件的绝对路径
script_path = os.path.abspath(sys.argv[0])
# 获取该脚本文件所在的目录路径
script_directory = os.path.dirname(script_path)
# 拼接要保存的 CSV 文件的完整路径
csv_file_path = os.path.join(script_directory, 'data.csv')


df = pd.read_csv(csv_file_path)

for i in range(9):
    task_struct.print_one_array(df.iloc[i])
