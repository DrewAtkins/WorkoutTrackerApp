let currentUser = null;

function signUp() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    
    fetch('/api/users/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        return response.json();
    })
    .then(user => {
        currentUser = user;
        showWorkoutContainer();
    })
    .catch(error => {
        console.error('Error:', error);
        if (error.error) {
            alert(error.error);
        } else {
            alert('An error occurred during sign up. Please try again.');
        }
    });
}

function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    
    fetch('/api/users/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        return response.json();
    })
    .then(user => {
        currentUser = user;
        showWorkoutContainer();
        fetchWorkouts();
    })
    .catch(error => {
        console.error('Error:', error);
        if (error.error) {
            alert(error.error);
        } else {
            alert('An error occurred during login. Please try again.');
        }
    });
}

function logout() {
    fetch('/api/users/logout', {
        method: 'POST',
    })
    .then(response => response.json())
    .then(data => {
        currentUser = null;
        showAuthContainer();
    })
    .catch(error => console.error('Error:', error));
}

function forgotPassword() {
    const email = document.getElementById('email').value;
    if (!email) {
        alert('Please enter your email address.');
        return;
    }

    fetch('/api/users/forgot-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email }),
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        return response.json();
    })
    .then(data => {
        alert('A password reset email has been sent to your email address.');
    })
    .catch(error => {
        console.error('Error:', error);
        if (error.error) {
            alert(error.error);
        } else {
            alert('An error occurred while processing your request. Please try again.');
        }
    });
}

function showWorkoutContainer() {
    document.getElementById('auth-container').style.display = 'none';
    document.getElementById('workout-container').style.display = 'block';
}

function showAuthContainer() {
    document.getElementById('auth-container').style.display = 'block';
    document.getElementById('workout-container').style.display = 'none';
}

function logWorkout() {
    const date = document.getElementById('workout-date').value;
    const description = document.getElementById('workout-description').value;
    
    fetch('/api/workouts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ 
            user: { id: currentUser.id },
            date: date,
            description: description
        }),
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        return response.json();
    })
    .then(workout => {
        fetchWorkouts();
        document.getElementById('workout-date').value = '';
        document.getElementById('workout-description').value = '';
    })
    .catch(error => {
        console.error('Error:', error);
        if (error.error) {
            alert(error.error);
        } else {
            alert('An error occurred while logging the workout. Please try again.');
        }
    });
}

function fetchWorkouts() {
    fetch(`/api/workouts/user/${currentUser.id}`)
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw err; });
        }
        return response.json();
    })
    .then(workouts => {
        const workoutList = document.getElementById('workout-list');
        workoutList.innerHTML = '';
        workouts.forEach(workout => {
            const li = document.createElement('li');
            li.textContent = `${new Date(workout.date).toLocaleString()}: ${workout.description}`;
            workoutList.appendChild(li);
        });
    })
    .catch(error => {
        console.error('Error:', error);
        if (error.error) {
            alert(error.error);
        } else {
            alert('An error occurred while fetching workouts. Please try again.');
        }
    });
}