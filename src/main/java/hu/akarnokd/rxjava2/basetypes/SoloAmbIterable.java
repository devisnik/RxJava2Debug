/*
 * Copyright 2016-2017 David Karnok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.akarnokd.rxjava2.basetypes;

import org.reactivestreams.Subscriber;

import hu.akarnokd.rxjava2.basetypes.SoloAmbArray.AmbSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.functions.ObjectHelper;

/**
 * Signal the events of the first source that signals.
 *
 * @param <T> the value type
 */
final class SoloAmbIterable<T> extends Solo<T> {

    final Iterable<? extends Solo<? extends T>> sources;

    SoloAmbIterable(Iterable<? extends Solo<? extends T>> sources) {
        this.sources = sources;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        AmbSubscriber<T> parent = new AmbSubscriber<T>(s);
        s.onSubscribe(parent);

        try {
            for (Solo<? extends T> source : sources) {
                ObjectHelper.requireNonNull(source, "One of the sources is null")
                .subscribe(parent);
            }
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            parent.onError(ex);
        }
    }
}
