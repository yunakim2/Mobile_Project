# Emotionary

📌 *나만의 감정 분석 다이어리*

<br>

- 2020-2학기) 모바일 프로그래밍 수업 팀 프로젝트 
- 프로젝트 기간 : 2020.10.01 ~ 2020.12.17

<br>

<br>

## Emotionary Service

다이어리에 일기와 사진을 작성하면 그에 기반하여 감정 분석 결과를 알려주는 다이어리 앱

감정 분석 기반으로 주 차별 분석 결과를 차트로 보여주고, 그 주차의 다이어리 사진들을 모아보기 할 수 있다.

<br>

<br>
## Block Diagram
![image](https://github.com/yunakim2/mobile_project/blob/master/image/%EA%B7%B8%EB%A6%BC5.png)
<br>
<br>
## Package Structure

![image](https://github.com/yunakim2/mobile_project/blob/master/image/package.png)
- */* : **Application Class**
- *adpater* : **RecyclerView Adapter**
- *analysis* : **Analysis Class / Data Class**
- *calendar* : **Calaendar Class / Data Class**
- *chart* : **AnalysisDetail - Chart Class / Data Class**
- *main* : **Splash, Main Class**
- *realmDB* : **RealmDB Setting Class / Data Class**
- *texttranslation* : **PapagoTranslationClass**

<br>

<br>

## Libraries

- ***Glide*** : Image loading framework(decoding, memory and disk caching ..)
- ***MPAndroidChart*** : Easy to use chart library
- ***Material Design*** : Material Components for Andriod by google

- **RealmDB** : Easy to use Database
- **tensorflow - lite - text_classification** : Analysis of positive and negative words trained on the IMDB dataset

<br>

<br>

## Git Convention

> **Cycle**

- Do Merge week a day.

<br>

> **Branch**

- master
  - <kbd>dohyunkim</kbd>
  - <kbd>yunakim</kbd>

<br>

> **Commit Message**

```
UPDATE - When implementing a function
FIX - When bugs was discovered
```

*ex) `UPDATE : 차트 만들기 및 분석 RecyclerView 구현`*

<br>

<br>
## 주요 화면
### 스플래쉬
![image](https://github.com/yunakim2/mobile_project/blob/master/image/%EA%B7%B8%EB%A6%BC4.png)
<br>
### 캘린더 뷰
![image](https://github.com/yunakim2/mobile_project/blob/master/image/1.png)
![image](https://github.com/yunakim2/mobile_project/blob/master/image/2.png)
<br>
### 분석 뷰
![image](https://github.com/yunakim2/mobile_project/blob/master/image/3.png)
<br>
### 차트 뷰
![image](https://github.com/yunakim2/mobile_project/blob/master/image/%EA%B7%B8%EB%A6%BC3.png)
<br>
### 통화기록 뷰
![image](https://github.com/yunakim2/mobile_project/blob/master/image/%EA%B7%B8%EB%A6%BC1.png)
![image](https://github.com/yunakim2/mobile_project/blob/master/image/%EA%B7%B8%EB%A6%BC2.png)
<br>
<br>
## Role

#### 유나

~~~
- 전반적인 디자인 및 뷰 
- 캘린더 다이어리 작성 dialog 뷰 / 기능
- 차트 뷰 / 기능
- RealmDB Database 구현
- 문자열 감정 분석 기능
- Papago 영어 -> 한국어 번역 기능
~~~

#### 도현

~~~
- 캘린더 뷰 / 기능
- 전화 걸기 뷰 / 기능
- 분석 뷰 - 리사이클러뷰
- 문자열 감정 분석 기능
- 날짜 차이 계산 기능
~~~



## Developers

* ![img](https://github.com/yunakim2.png?size=100) [*Kim Yuna*](https://github.com/yunakim2)
* *[Kim dohyun](https://github.com/dohyunKim12)*





