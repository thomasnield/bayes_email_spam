# Bayes Email Spam Filter

Experimenting with Bayes and Email Spam Categorization in Kotlin. No dependencies needed besides Kotlin 1.2 std-lib. 

Chapter 13 of [Data Science from Scratch](https://www.safaribooksonline.com/library/view/data-science-from/9781491901410/ch13.html#naive_bayes) has been the most helpful resource for this project. 

**OUTPUT:**

```kotlin 
Score for an email containing message: "discount viagra wholesale, hurry while this offer lasts"
0.9990090904079181

Score for an email containing message: "interesting meeting on amazon cloud services discount program"
0.01754275128116032

Spammiest Words
viagra 0.7
this 0.5
for 0.5
prescription 0.5
hey 0.3

Hammiest (non-spam) Words
your 0.7857142857142857
amazon 0.5
for 0.35714285714285715
a 0.35714285714285715
meeting 0.35714285714285715
```
