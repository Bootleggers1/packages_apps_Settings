/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo;

import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.VolumeRecord;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.logging.MetricsLogger;
import com.android.settings.InstrumentedFragment;
import com.android.settings.R;

public class PrivateVolumeForget extends InstrumentedFragment {
    private VolumeRecord mRecord;

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.DEVICEINFO_STORAGE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final StorageManager storage = getActivity().getSystemService(StorageManager.class);
        final String fsUuid = getArguments().getString(VolumeRecord.EXTRA_FS_UUID);
        mRecord = storage.findRecordByUuid(fsUuid);

        final View view = inflater.inflate(R.layout.storage_internal_forget, container, false);
        final TextView body = (TextView) view.findViewById(R.id.body);
        final Button confirm = (Button) view.findViewById(R.id.confirm);

        body.setText(TextUtils.expandTemplate(getText(R.string.storage_internal_forget_details),
                mRecord.getNickname()));
        confirm.setOnClickListener(mConfirmListener);

        return view;
    }

    private final OnClickListener mConfirmListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final StorageManager storage = getActivity().getSystemService(StorageManager.class);
            storage.forgetVolume(mRecord.getFsUuid());
            getActivity().finish();
        }
    };
}
