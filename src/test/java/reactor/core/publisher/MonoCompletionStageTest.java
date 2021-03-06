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
package reactor.core.publisher;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MonoCompletionStageTest {


	@Test
	public void fromCompletableFuture(){
		CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> "helloFuture");

		assertThat(Mono.fromFuture(f)
		               .block()).isEqualToIgnoringCase("helloFuture");
	}

	@Test
	public void fromCompletionStage() {
		CompletionStage<String> completionStage = CompletableFuture.supplyAsync(() -> "helloFuture");

		assertThat(Mono.fromCompletionStage(completionStage).block())
				.isEqualTo("helloFuture");
	}
}