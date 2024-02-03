package com.example.cs_2340_student_scheduler_app.ui.exams;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExamViewModel extends ViewModel {


        private final MutableLiveData<String> mText;

        public ExamViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("This is notifications fragment");
        }

        public LiveData<String> getText() {
            return mText;
        }
}
