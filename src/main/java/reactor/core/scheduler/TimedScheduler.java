/*
 * Copyright (c) 2011-2016 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.core.scheduler;

/**
 * Provides an abstract, timed asynchronous boundary to operators.
 *
 * @deprecated all methods of this interface have been moved to {@link Scheduler} and
 * reactor-core implementations should now all be time-capable. Will be removed in 3.1.0.
 */
@Deprecated
public interface TimedScheduler extends Scheduler {

	@Override
	TimedWorker createWorker();

	/**
	 * @deprecated all methods of this interface have been moved to {@link Scheduler.Worker} and
	 * reactor-core implementations should now all be time-capable. Will be removed in 3.1.0.
	 */
	@Deprecated
	interface TimedWorker extends Worker {
	}
}
