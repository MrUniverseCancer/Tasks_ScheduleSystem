import numpy as np
import pandas as pd

structure = np.array(['TaskName', 'LeftTime', 'ImportanceLevel', 'TaskConsuming', 
                      'PunishLevel', 'PreferenceLevel', 'DifficultyLevel', 
                      'BufferbtwTasks', 'singalTaskTime', 'FactImportance', 'FactUrgency'])


# print one array for show
def print_one_array(array):
    for i in range(11):
        name = structure[i]
        print(name, ":", array[name])
    print()
    