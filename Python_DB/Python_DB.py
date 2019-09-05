import urllib.request
import openpyxl
from openpyxl import load_workbook
import json
import pandas as pd
import numpy as np
import requests
from collections import OrderedDict
import time 

class GradientDescent():
    def __init__(self, learning_rate=0.01, threshold=0.01, max_iterations=1000):
        self._learning_rate = learning_rate
        self._threshold = threshold
        self._max_iterations = max_iterations
        self._W = None

    def fit(self, x_data, y_data):
        num_examples, num_features = np.shape(x_data)
        self._W = np.ones(num_features)
        x_data_transposed = x_data.transpose()

        for i in range(self._max_iterations):
            # 실제값과 예측값의 차이
            diff = np.dot(x_data, self._W) - y_data

            # diff를 이용하여 cost 생성 : 오차의 제곱합 / 2 * 데이터 개수
            cost = np.sum(diff ** 2) / (2 * num_examples)

            # transposed X * cost / n
            gradient = np.dot(x_data_transposed, diff) / num_examples

            # W벡터 업데이트
            self._W = self._W - self._learning_rate * gradient

            # 판정 임계값에 다다르면 학습 중단
            if cost < self._threshold:
                return self._W

        return self._W




if __name__ == "__main__":    
    URL = 'http://ec2-3-17-158-59.us-east-2.compute.amazonaws.com/SendAndroid.php'


    #배열 초기화
    temp=[25,24]
    hum=[80,79]
    sound=[20,21]
    light=[80,82]
    Good=[0.7,0.6]
    Bad=[0.3,0.4]


    #DB값을 가져온다
    response = urllib.request.urlopen(URL)
    data = json.loads(response.read())
    
    

    #배열에 Sensor json값을 할당 
    for item in data:
        temp.append(float(item['temperature']))
        hum.append(float(item['humidity']))
        sound.append(float(item['sound']))
        light.append(float(item['light']))
        Bad.append(float(item['anger'])+float(item['contempt'])+float(item['fear'])+float(item['disgust'])+float(item['sadness'])+float(item['surprise']))

    
    #배열을 기반으로 Excel 파일을 만든다
    dr = pd.DataFrame({"Bad": Bad})    
    writer = pd.ExcelWriter('DB_emotion.xlsx', engine='xlsxwriter')
    print(dr) 
    # DataFrame을 xlsx에 쓰기
    dr_2 = dr.set_index("Bad")

    dr_2.to_excel(writer, sheet_name='Sheet1')
    

    df = pd.DataFrame({"temperature": temp, "humidity": hum, "sound": sound, "light": light})    
    writer = pd.ExcelWriter('DB_sensor.xlsx', engine='xlsxwriter')
    print(df) 
    # DataFrame을 xlsx에 쓰기
    df_2 = df.set_index("temperature")
    df_2.to_excel(writer, sheet_name='Sheet1')

   

    #엑셀 파일을 가져와 분석 시작
    
    #4가지 센서(온도, 습도, 조도, 소음)엑셀 파일을 가져와 데이터 프레임 만든다
    df_r=pd.read_excel('DB_sensor.xlsx',sheet_name='Sheet1')

    data_sensor=pd.DataFrame(df_r)

    x_data=np.array(data_sensor)



    #나쁜 감정 결과 엑셀 파일을 가져와 데이터 프레임 만든다
    emotion_array=[]

    dr_r=pd.read_excel('DB_emotion.xlsx',sheet_name='Sheet1')

    data_emotion=pd.DataFrame(dr_r)

    temp=data_emotion.values.T.tolist()

    y_data=np.array(temp[0])
    
    
        
    
    x_normed = (x_data - x_data.min()) / (x_data.max() - x_data.min())

    optimizer2 = GradientDescent()
    coeffs = optimizer2.fit(x_data=x_normed, y_data=y_data)
   

    file_data = OrderedDict()

    file_data['temperature']='%s'%coeffs[0]

    file_data['humidity']='%s'%coeffs[1]

    file_data['sound']='%s'%coeffs[2]
    
    file_data['light']='%s'%coeffs[3]
    

    result = json.dumps(file_data , ensure_ascii=False )

    print(result)

    with open('DBanal.txt','w', encoding="utf-8") as make_file:
        json.dump(file_data, make_file, ensure_ascii=False)

    f=open('DBanal.txt','r')

    line=f.readline()

    print(line)

    f.close
    
    
    URL = 'http://ec2-3-17-158-59.us-east-2.compute.amazonaws.com/SendAnalDB.php'



    headers = {'content-type': 'application/json'}


 

    #다중 회귀 분석 결과인 계수를 서버로 보낸다
    response= requests.post(URL, data=line,headers = headers)

    print(response)


    

    


