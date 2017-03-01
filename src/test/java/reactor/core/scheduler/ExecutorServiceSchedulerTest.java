/*
 * Copyright (c) 2011-2017 Pivotal Software Inc, All Rights Reserved.
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

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler.Worker;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Stephane Maldini
 */
public class ExecutorServiceSchedulerTest extends AbstractSchedulerTest {

	@Override
	protected Scheduler scheduler() {
		return Schedulers.fromExecutor(Executors.newSingleThreadScheduledExecutor());
	}

	@Override
	protected boolean shouldCheckDisposeTask() {
		return false;
	}

	@Test
	public void noopCancelledAndFinished() throws Exception {
		ExecutorServiceScheduler.EMPTY.run();
	}

	@Test
	public void notScheduledRejects() {
		Scheduler s = Schedulers.fromExecutorService(Executors.newSingleThreadExecutor());
		assertThat(s.schedule(() -> {}, 100, TimeUnit.MILLISECONDS))
				.describedAs("direct delayed scheduling")
				.isSameAs(Scheduler.NOT_TIMED);
		assertThat(s.schedulePeriodically(() -> {}, 100, 100, TimeUnit.MILLISECONDS))
				.describedAs("direct periodic scheduling")
				.isSameAs(Scheduler.NOT_TIMED);

		Worker w = s.createWorker();
		assertThat(w.schedule(() -> {}, 100, TimeUnit.MILLISECONDS))
				.describedAs("worker delayed scheduling")
				.isSameAs(Scheduler.NOT_TIMED);
		assertThat(w.schedulePeriodically(() -> {}, 100, 100, TimeUnit.MILLISECONDS))
				.describedAs("worder periodic scheduling")
				.isSameAs(Scheduler.NOT_TIMED);
	}

	@Test
	public void scheduledDoesntReject() {
		Scheduler s = Schedulers.fromExecutorService(Executors.newSingleThreadScheduledExecutor());
		assertThat(s.schedule(() -> {}, 100, TimeUnit.MILLISECONDS))
				.describedAs("direct delayed scheduling")
				.isNotInstanceOf(RejectedDisposable.class);
		assertThat(s.schedulePeriodically(() -> {}, 100, 100, TimeUnit.MILLISECONDS))
				.describedAs("direct periodic scheduling")
				.isNotInstanceOf(RejectedDisposable.class);

		Worker w = s.createWorker();
		assertThat(w.schedule(() -> {}, 100, TimeUnit.MILLISECONDS))
				.describedAs("worker delayed scheduling")
				.isNotInstanceOf(RejectedDisposable.class);
		assertThat(w.schedulePeriodically(() -> {}, 100, 100, TimeUnit.MILLISECONDS))
				.describedAs("worker periodic scheduling")
				.isNotInstanceOf(RejectedDisposable.class);
	}

	@Test
	public void smokeTestDelay() {
		Scheduler s = scheduler();

		try {
			StepVerifier.create(Mono.delay(Duration.ofMillis(10), scheduler()))
		                .expectSubscription()
		                .expectNoEvent(Duration.ofMillis(10))
		                .expectNext(0L)
		                .verifyComplete();
		}
		finally {
			s.dispose();
		}
	}

	@Test
	public void smokeTestInterval() {
		Scheduler s = scheduler();

		try {
			StepVerifier.create(Flux.interval(Duration.ofMillis(5), Duration.ofMillis(10), scheduler()))
			            .expectSubscription()
			            .expectNoEvent(Duration.ofMillis(5))
			            .expectNext(0L)
			            .expectNoEvent(Duration.ofMillis(10))
			            .expectNext(1L)
			            .expectNoEvent(Duration.ofMillis(10))
			            .expectNext(2L)
			            .thenCancel();
		}
		finally {
			s.dispose();
		}
	}
}