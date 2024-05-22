package com.nutrienviroment.nutrigenda.screens.nutritionist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrienviroment.nutrigenda.R;
import com.nutrienviroment.nutrigenda.models.user.User;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<User> patientList;
    private OnPatientClickListener onPatientClickListener;

    public interface OnPatientClickListener {
        void onPatientClick(User patient);
    }

    public PatientAdapter(List<User> patientList, OnPatientClickListener onPatientClickListener) {
        this.patientList = patientList;
        this.onPatientClickListener = onPatientClickListener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        User patient = patientList.get(position);
        holder.tvPatientName.setText(patient.getName());
        holder.tvPatientEmail.setText(patient.getEmail());
        holder.itemView.setOnClickListener(v -> onPatientClickListener.onPatientClick(patient));
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName;
        TextView tvPatientEmail;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvPatientEmail = itemView.findViewById(R.id.tvPatientEmail);
        }
    }
}
